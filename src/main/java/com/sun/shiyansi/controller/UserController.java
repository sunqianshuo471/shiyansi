package com.sun.shiyansi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.shiyansi.common.Result;
import com.sun.shiyansi.entity.User;
import com.sun.shiyansi.entity.UserInfo;
import com.sun.shiyansi.service.UserService;
import com.sun.shiyansi.vo.UserDetailVO;
import jakarta.annotation.Resource; // 必须用jakarta包，不是javax
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
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

    /**
     * 查询用户详情（多表联查 + Redis）
     * @param userId 用户ID
     * @return 用户详情
     */
    @GetMapping("/{id}/detail")
    public Result<UserDetailVO> getUserDetail(@PathVariable("id") Long userId) {
        return userService.getUserDetail(userId);
    }

    /**
     * 更新用户扩展信息
     * @param userId 用户ID
     * @param userInfo 用户扩展信息
     * @return 操作结果
     */
    @PutMapping("/{id}/detail")
    public Result<String> updateUserInfo(@PathVariable("id") Long userId, @RequestBody UserInfo userInfo) {
        userInfo.setUserId(userId);
        return userService.updateUserInfo(userInfo);
    }

    /**
     * 删除用户
     * @param userId 用户ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable("id") Long userId) {
        return userService.deleteUser(userId);
    }

    /**
     * 用户登录
     * @param user 用户信息（包含username和password）
     * @return 操作结果
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody User user) {
        return userService.login(user.getUsername(), user.getPassword());
    }

    /**
     * 用户注册
     * @param user 用户信息
     * @return 操作结果
     */
    @PostMapping
    public Result<String> register(@RequestBody User user) {
        return userService.register(user);
    }
}