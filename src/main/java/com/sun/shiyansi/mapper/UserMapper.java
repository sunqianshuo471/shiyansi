package com.sun.shiyansi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.shiyansi.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
