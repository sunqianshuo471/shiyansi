package com.sun.shiyansi.common;

public enum ResultCode {
    SUCCESS(200, "操作成功"),
    ERROR(500, "系统错误"),
    // 追加以下状态码 [cite: 34, 35, 36]
    USER_HAS_EXISTED(4001, "该用户名已被注册"),
    USER_NOT_EXIST(4002, "该用户不存在"),
    PASSWORD_ERROR(4003, "账号或密码错误"),
    PARAM_ERROR(4004, "参数错误");

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


    public Integer getCode() { return code; }
    public String getMessage() { return message; }
}