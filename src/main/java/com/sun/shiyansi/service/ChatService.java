package com.sun.shiyansi.service;

import com.sun.shiyansi.dto.ChatRequestDTO;
import com.sun.shiyansi.vo.ChatResponseVO;

public interface ChatService {
    ChatResponseVO chat(ChatRequestDTO requestDTO);
}