package com.sun.shiyansi.controller;

import com.sun.shiyansi.common.Result;
import com.sun.shiyansi.dto.UserDTO;
import com.sun.shiyansi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController // [cite: 98, 236]
@RequestMapping("/api/users") // [cite: 99, 237]
public class UserController {

    @Autowired
    private UserService userService; // [cite: 101, 240]

    // 1. 用户注册 [cite: 103, 241]
    @PostMapping
    public Result<String> register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO); // [cite: 106, 245]
    }

    // 2. 用户登录 [cite: 108, 246]
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO); // [cite: 111, 249]
    }

    // 3. 根据 ID 查询用户 [cite: 251]
    @GetMapping("/{id}")
    public Result<String> getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id); // [cite: 254]
    }
}