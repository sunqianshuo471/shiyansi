package com.sun.shiyansi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sun.shiyansi.common.Result;
import com.sun.shiyansi.common.ResultCode;
import com.sun.shiyansi.dto.UserDTO;
import com.sun.shiyansi.entity.User;
import com.sun.shiyansi.mapper.UserMapper;
import com.sun.shiyansi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // 将业务类交给 Spring 容器管理 [cite: 67, 204]
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper; // 注入 Mapper 接口 [cite: 206, 207]

    @Override
    public Result<String> register(UserDTO userDTO) {
        // 1. 查询该用户名是否已存在 [cite: 210]
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername()); // 拼接查询条件 [cite: 211]
        User dbUser = userMapper.selectOne(queryWrapper); // 执行查询 [cite: 212]

        if (dbUser != null) {
            return Result.error(ResultCode.USER_HAS_EXISTED); // 用户已存在错误 [cite: 215]
        }

        // 2. 组装实体对象并插入数据库 [cite: 216, 220]
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        userMapper.insert(user); // 执行插入 [cite: 221]

        return Result.success("注册成功!"); // [cite: 222]
    }

    @Override
    public Result<String> login(UserDTO userDTO) {
        // 1. 根据用户名查询数据库 [cite: 226, 227]
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername());
        User dbUser = userMapper.selectOne(queryWrapper);

        // 2. 校验用户是否存在 [cite: 230]
        if (dbUser == null) {
            return Result.error(ResultCode.USER_NOT_EXIST); // [cite: 232]
        }

        // 3. 校验密码是否正确
        if (!dbUser.getPassword().equals(userDTO.getPassword())) {
            return Result.error(ResultCode.PASSWORD_ERROR);
        }

        return Result.success("登录成功!");
    }

    @Override
    public Result<String> getUserById(Long id) {
        // 根据 ID 查询用户真实数据
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        return Result.success("查询成功，用户名为：" + user.getUsername());
    }
}