package com.sun.shiyansi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sun.shiyansi.mapper") // 扫描 Mapper 接口所在的包
public class ShiyansiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShiyansiApplication.class, args);
    }
}