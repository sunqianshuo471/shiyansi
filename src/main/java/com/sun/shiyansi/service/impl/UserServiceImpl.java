package com.sun.shiyansi.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.shiyansi.common.Result;
import com.sun.shiyansi.common.ResultCode;
import com.sun.shiyansi.entity.User;
import com.sun.shiyansi.entity.UserInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sun.shiyansi.mapper.UserInfoMapper;
import com.sun.shiyansi.mapper.UserMapper;
import com.sun.shiyansi.service.UserService;
import com.sun.shiyansi.vo.UserDetailVO;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * 用户Service实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final String CACHE_KEY_PREFIX = "user:detail:";

    @org.springframework.beans.factory.annotation.Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    @Resource
    private UserInfoMapper userInfoMapper;

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

    @Override
    public Result<UserDetailVO> getUserDetail(Long userId) {
        String key = CACHE_KEY_PREFIX + userId;

        // 1. 先查缓存（只有Redis可用时才使用缓存）
        if (redisTemplate != null) {
            String json = redisTemplate.opsForValue().get(key);
            if (json != null && !json.isBlank()) {
                try {
                    UserDetailVO cacheVO = JSONUtil.toBean(json, UserDetailVO.class);
                    return Result.success(cacheVO);
                } catch (Exception e) {
                    // 缓存数据异常，删掉脏缓存，继续查数据库
                    redisTemplate.delete(key);
                }
            }
        }

        // 2. 查数据库
        UserDetailVO detail = userInfoMapper.getUserDetail(userId);
        if (detail == null) {
            return Result.fail(ResultCode.USER_NOT_EXIST.getCode(), ResultCode.USER_NOT_EXIST.getMessage());
        }

        // 3. 写缓存（只有Redis可用时才写入缓存）
        if (redisTemplate != null) {
            redisTemplate.opsForValue().set(
                    key,
                    JSONUtil.toJsonStr(detail),
                    10,
                    TimeUnit.MINUTES
            );
        }

        return Result.success(detail);
    }

    @Override
    @Transactional
    public Result<String> updateUserInfo(UserInfo userInfo) {
        // 参数校验,userInfo 不能为空，并且 userId 不能为空,后面删除 Redis 缓存时需要
        if (userInfo == null || userInfo.getUserId() == null) {
            return Result.fail(ResultCode.PARAM_ERROR.getCode(), ResultCode.PARAM_ERROR.getMessage());
        }

        // 更新数据库
        userInfoMapper.updateById(userInfo);

        // 删除缓存（只有Redis可用时才删除缓存）
        if (redisTemplate != null) {
            String key = CACHE_KEY_PREFIX + userInfo.getUserId();
            redisTemplate.delete(key);
        }

        return Result.success("更新成功");
    }

    @Override
    @Transactional
    public Result<String> deleteUser(Long userId) {
        // 删除用户基础信息
        this.removeById(userId);

        // 删除用户扩展信息
        userInfoMapper.deleteById(userId);

        // 删除缓存（只有Redis可用时才删除缓存）
        if (redisTemplate != null) {
            String key = CACHE_KEY_PREFIX + userId;
            redisTemplate.delete(key);
        }

        return Result.success("删除成功");
    }

    @Override
    public Result<String> login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return Result.fail(ResultCode.PARAM_ERROR.getCode(), ResultCode.PARAM_ERROR.getMessage());
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = this.getOne(queryWrapper);

        if (user == null) {
            return Result.fail(ResultCode.USER_NOT_EXIST.getCode(), ResultCode.USER_NOT_EXIST.getMessage());
        }

        if (!password.equals(user.getPassword())) {
            return Result.fail(4003, "密码错误");
        }

        return Result.success("登录成功");
    }

    @Override
    @Transactional
    public Result<String> register(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().isBlank()) {
            return Result.fail(ResultCode.PARAM_ERROR.getCode(), ResultCode.PARAM_ERROR.getMessage());
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, user.getUsername());
        if (this.count(queryWrapper) > 0) {
            return Result.fail(4004, "用户名已存在");
        }

        this.save(user);
        return Result.success("注册成功");
    }
}