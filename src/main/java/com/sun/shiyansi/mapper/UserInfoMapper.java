package com.sun.shiyansi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.shiyansi.entity.UserInfo;
import com.sun.shiyansi.vo.UserDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    @Select("""
        SELECT
            u.id AS userId,
            u.username,
            i.real_name AS realName,
            i.phone,
            i.address
        FROM sys_user u
        LEFT JOIN user_info i ON u.id = i.user_id
        WHERE u.id = #{userId}
    """)
    UserDetailVO getUserDetail(@Param("userId") Long userId);
}