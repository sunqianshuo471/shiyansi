package com.sun.shiyansi.service.impl;

import com.sun.shiyansi.common.Result;
import com.sun.shiyansi.common.ResultCode;
import com.sun.shiyansi.dto.UserDTO;
import com.sun.shiyansi.service.UserService;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service // 将业务类交给 Spring 容器管理 [cite: 67]
public class UserServiceImpl implements UserService {
    // 暂时使用 Map 模拟数据库 [cite: 69]
    private static final Map<String, String> userDb = new HashMap<>();

    @Override
    public Result<String> register(UserDTO userDTO) {
        // 1. 校验用户是否已存在 [cite: 72]
        if (userDb.containsKey(userDTO.getUsername())) {
            return Result.error(ResultCode.USER_HAS_EXISTED); // [cite: 75]
        }
        // 2. 存入模拟数据库 [cite: 76]
        userDb.put(userDTO.getUsername(), userDTO.getPassword()); // [cite: 77]
        return Result.success("注册成功"); // [cite: 78]
    }

    @Override
    public Result<String> login(UserDTO userDTO) {
        // 1. 校验用户是否存在 [cite: 82]
        if (!userDb.containsKey(userDTO.getUsername())) {
            return Result.error(ResultCode.USER_NOT_EXIST); // [cite: 85]
        }
        // 2. 校验密码是否正确 [cite: 86]
        String dbPassword = userDb.get(userDTO.getUsername()); // [cite: 87]
        if (!dbPassword.equals(userDTO.getPassword())) {
            return Result.error(ResultCode.PASSWORD_ERROR); // [cite: 88]
        }
        return Result.success("登录成功");
    }
}