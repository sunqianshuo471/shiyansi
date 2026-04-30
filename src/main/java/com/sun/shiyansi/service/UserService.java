package com.sun.shiyansi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.shiyansi.common.Result;
import com.sun.shiyansi.entity.User;
import com.sun.shiyansi.entity.UserInfo;
import com.sun.shiyansi.vo.UserDetailVO;

/**
 * 用户Service接口
 */
public interface UserService extends IService<User> {

    /**
     * 分页查询用户列表
     * @param pageNum  当前页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    Result<Page<User>> getUserPage(Integer pageNum, Integer pageSize);

    /**
     * 获取用户详情
     * @param userId 用户ID
     * @return 用户详情
     */
    Result<UserDetailVO> getUserDetail(Long userId);

    /**
     * 更新用户扩展信息
     * @param userInfo 用户扩展信息
     * @return 操作结果
     */
    Result<String> updateUserInfo(UserInfo userInfo);

    /**
     * 删除用户
     * @param userId 用户ID
     * @return 操作结果
     */
    Result<String> deleteUser(Long userId);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 操作结果
     */
    Result<String> login(String username, String password);

    /**
     * 用户注册
     * @param user 用户信息
     * @return 操作结果
     */
    Result<String> register(User user);
}