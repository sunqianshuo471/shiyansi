package com.sun.shiyansi.controller;

import com.sun.shiyansi.common.Result;
import com.sun.shiyansi.dto.ChatRequestDTO;
import com.sun.shiyansi.service.ChatService;
import com.sun.shiyansi.vo.ChatResponseVO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public Result<ChatResponseVO> chat(@RequestBody ChatRequestDTO requestDTO) {
        ChatResponseVO responseVO = chatService.chat(requestDTO);
        return Result.success(responseVO);
    }
}