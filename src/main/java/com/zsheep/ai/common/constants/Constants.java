package com.zsheep.ai.common.constants;

import io.jsonwebtoken.Claims;

/**
 * 通用常量信息
 *
 * @author ruoyi
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";
    
    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";
    
    /**
     * 登录成功后的后台标识
     */
    public static final String TOKEN_KEY = "Authorization";
    
    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";
    
    /**
     * http请求
     */
    public static final String HTTP = "http://";
    
    /**
     * https请求
     */
    public static final String HTTPS = "https://";
    
    /**
     * 注册
     */
    public static final String REGISTER = "Register";
    
    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;
    
    /**
     * 令牌
     */
    public static final String TOKEN = "token";
    
    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";
    
    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";
    
    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = Claims.SUBJECT;
    
    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";
    
    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";
    public static final String LOGIN_USER_ID_KEY = "login_user_id";
}
