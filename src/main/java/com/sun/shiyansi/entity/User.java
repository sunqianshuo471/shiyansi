package com.sun.shiyansi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user") // 绑定数据库表名 [cite: 11]
public class User {
    @TableId(type = IdType.AUTO) // 主键自增 [cite: 13, 14]
    private Long id;
    private String username;
    private String password;
}