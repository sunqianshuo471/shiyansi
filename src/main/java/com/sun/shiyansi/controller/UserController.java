package com.sun.shiyansi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.shiyansi.common.Result;
import com.sun.shiyansi.entity.User;
import com.sun.shiyansi.service.UserService;
import jakarta.annotation.Resource; // 必须用jakarta包，不是javax
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Resource // 注解生效，自动注入userService，消除警告
    private UserService userService;

    /**
     * 分页查询用户接口
     * @param pageNum  当前页码（默认第1页）
     * @param pageSize 每页条数（默认10条）
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<Page<User>> getUserPage(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return userService.getUserPage(pageNum, pageSize);
    }
}