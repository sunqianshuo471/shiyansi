package com.sun.shiyansi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.shiyansi.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper // 标识为 MyBatis Mapper [cite: 174]
public interface UserMapper extends BaseMapper<User> {
    // 继承 BaseMapper 后无需手写基础 SQL [cite: 170, 176]
}