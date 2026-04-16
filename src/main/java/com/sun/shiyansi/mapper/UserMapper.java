package com.sun.shiyansi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.shiyansi.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 无需手动写分页方法，BaseMapper已经内置了selectPage
}