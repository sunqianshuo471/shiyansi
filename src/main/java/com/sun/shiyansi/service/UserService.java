package com.sun.shiyansi.service;

import com.sun.shiyansi.common.Result;
import com.sun.shiyansi.dto.UserDTO;

public interface UserService {
    Result<String> register(UserDTO userDTO); // 用户注册 [cite: 198]
    Result<String> login(UserDTO userDTO);    // 用户登录 [cite: 199]
    Result<String> getUserById(Long id);      // 根据 ID 查询用户 [cite: 200]
}