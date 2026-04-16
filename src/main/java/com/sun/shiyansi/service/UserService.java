package com.sun.shiyansi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.shiyansi.common.Result;
import com.sun.shiyansi.entity.User;

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
}