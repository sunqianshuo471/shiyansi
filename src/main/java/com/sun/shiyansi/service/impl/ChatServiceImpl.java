package com.sun.shiyansi.service.impl;

import com.sun.shiyansi.dto.ChatRequestDTO;
import com.sun.shiyansi.service.ChatService;
import com.sun.shiyansi.vo.ChatResponseVO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService {

    @Value("${spring.ai.dashscope.api-key:}")
    private String apiKey;

    private static final int MAX_HISTORY_ROUNDS = 3;
    private static final String REDIS_KEY_PREFIX = "chat:session:";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate stringRedisTemplate;

    public ChatServiceImpl(RestTemplate restTemplate, StringRedisTemplate stringRedisTemplate) {
        this.restTemplate = restTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public ChatResponseVO chat(ChatRequestDTO requestDTO) {
        String sessionId = requestDTO.getSessionId();
        String message = requestDTO.getMessage();

        if (apiKey == null || apiKey.isEmpty()) {
            return new ChatResponseVO(message, "聊天功能暂未配置，请联系管理员配置DashScope API Key。");
        }

        if (sessionId == null || sessionId.isEmpty()) {
            return new ChatResponseVO(message, "会话ID不能为空");
        }

        try {
            String redisKey = REDIS_KEY_PREFIX + sessionId;

            List<String> records = stringRedisTemplate.opsForList().range(redisKey, 0, -1);

            String historyText = "";
            if (records != null && !records.isEmpty()) {
                historyText = String.join("\n", records);
            }

            String finalPrompt = """
                    以下是历史对话：
                    %s

                    当前用户问题：
                    %s
                    """.formatted(historyText, message);

            String answer = callDashScopeAPI(finalPrompt);

            String recordText = "用户：" + message + "\n助手：" + answer;
            stringRedisTemplate.opsForList().rightPush(redisKey, recordText);

            Long size = stringRedisTemplate.opsForList().size(redisKey);
            if (size != null && size > MAX_HISTORY_ROUNDS) {
                stringRedisTemplate.opsForList().trim(redisKey, size - MAX_HISTORY_ROUNDS, size - 1);
            }

            return new ChatResponseVO(message, answer);

        } catch (Exception e) {
            return new ChatResponseVO(message, "聊天服务调用失败: " + e.getMessage());
        }
    }

    private String callDashScopeAPI(String prompt) {
        String url = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "qwen-turbo");

        Map<String, String> input = new HashMap<>();
        input.put("prompt", prompt);
        requestBody.put("input", input);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("max_tokens", 2048);
        parameters.put("temperature", 0.8);
        requestBody.put("parameters", parameters);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode output = root.get("output");
                if (output != null) {
                    JsonNode text = output.get("text");
                    if (text != null) {
                        return text.asText();
                    }
                }
                return "未获取到响应内容";
            } catch (Exception e) {
                return "响应解析失败: " + e.getMessage();
            }
        }
        return "API调用失败，状态码: " + response.getStatusCode();
    }
}