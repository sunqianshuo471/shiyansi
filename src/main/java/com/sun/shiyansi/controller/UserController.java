package com.sun.shiyansi.controller;

import com.sun.shiyansi.common.Result;
import com.sun.shiyansi.dto.UserDTO;
import com.sun.shiyansi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users") // [cite: 99]
public class UserController {
    @Autowired
    private UserService userService; // [cite: 102]

    // 1. 注册接口 - POST /api/users [cite: 103, 104]
    @PostMapping
    public Result<String> register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO); // [cite: 106]
    }

    // 2. 登录接口 - POST /api/users/login [cite: 108, 109]
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO); // [cite: 111]
    }

    // 3. 获取用户信息 - 用于测试拦截器放行 [cite: 113, 114]
    @GetMapping("/{id}")
    public Result<String> getUser(@PathVariable("id") Long id) {
        return Result.success("查询成功,正在返回 ID为" + id + "的用户信息"); // [cite: 116]
    }
}