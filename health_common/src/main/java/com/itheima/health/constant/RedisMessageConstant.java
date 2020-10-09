package com.itheima.health.constant;

public interface RedisMessageConstant {
    static final String SENDTYPE_ORDER = "001";//用于缓存体检预约时发送的验证码
    static final String SENDTYPE_LOGIN = "002";//用于缓存手机号快速登录时发送的验证码
    static final String SENDTYPE_GETPWD = "003";//用于缓存找回密码时发送的验证码
    static final String SETMEAL_GETLIST = "004";//用于缓存查询套餐列表
    static final String MEALDETAIL = "005";//用于缓存查询套餐列表
}