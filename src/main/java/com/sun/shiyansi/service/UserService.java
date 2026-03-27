package com.sun.shiyansi.service;

import com.sun.shiyansi.common.Result;
import com.sun.shiyansi.dto.UserDTO;

public interface UserService {
    Result<String> register(UserDTO userDTO);
    Result<String> login(UserDTO userDTO);
}