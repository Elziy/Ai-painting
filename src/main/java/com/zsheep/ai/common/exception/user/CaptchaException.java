package com.zsheep.ai.common.exception.user;

/**
 * 验证码错误异常类
 *
 * @author Elziy
 */
public class CaptchaException extends UserException {
    private static final long serialVersionUID = 1L;
    
    public CaptchaException() {
        super("user.captcha.error", null);
    }
}
