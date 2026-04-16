package com.sun.shiyansi.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.shiyansi.common.Result;
import com.sun.shiyansi.entity.User;
import com.sun.shiyansi.mapper.UserMapper;
import com.sun.shiyansi.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Result<Page<User>> getUserPage(Integer pageNum, Integer pageSize) {
        // 1. 校验分页参数，设置默认值
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }

        // 2. 创建分页对象（参数：当前页、每页条数）
        Page<User> pageParam = new Page<>(pageNum, pageSize);

        // 3. 执行分页查询（null表示无查询条件，会查询所有用户）
        Page<User> resultPage = this.page(pageParam, null);

        // 4. 返回封装好的分页结果
        return Result.success(resultPage);
    }
}