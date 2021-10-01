package com.yawen.blog.vo;


public enum ErrorCode {
    PARAMS_ERROR(10001,"参数错误"),
    ACCOUNT_PWD_NOT_EXIST(10002,"用户名或密码不正确"),
    NO_PERMISSION(70001,"无权访问"),
    SESSION_TIME_OUT(90001,"会话超时"),
    NO_LOGIN(90002,"未登录"),
    ACCOUNT_EXIST(10002,"账号已存在");
    private int code;
    private String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
