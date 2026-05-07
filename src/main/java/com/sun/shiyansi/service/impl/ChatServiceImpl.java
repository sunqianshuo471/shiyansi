package com.sun.shiyansi.service.impl;

import com.sun.shiyansi.service.ChatService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatServiceImpl implements ChatService {

    @Value("${spring.ai.dashscope.api-key:}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ChatServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public String chat(String message) {
        if (apiKey == null || apiKey.isEmpty()) {
            return "聊天功能暂未配置，请联系管理员配置DashScope API Key。";
        }

        try {
            String url = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "qwen-turbo");

            Map<String, String> input = new HashMap<>();
            input.put("prompt", message);
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
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode output = root.get("output");
                if (output != null) {
                    JsonNode text = output.get("text");
                    if (text != null) {
                        return text.asText();
                    }
                }
                return "未获取到响应内容";
            }
            return "API调用失败，状态码: " + response.getStatusCode();
        } catch (Exception e) {
            return "聊天服务调用失败: " + e.getMessage();
        }
    }
}