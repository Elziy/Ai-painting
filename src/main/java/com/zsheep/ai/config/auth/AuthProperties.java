package com.zsheep.ai.config.auth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ai.auth")
public class AuthProperties {
    /**
     * 密码最大重试次数
     */
    private Integer passwordMaxRetryCount = 5;
    
    /**
     * 密码锁定时间(分钟)
     */
    private Integer passwordLockTime = 10;
    
    /**
     * 默认头像
     */
    private String defaultAvatar = "";
    
    /**
     * 令牌头部key
     */
    private String tokenHeaderKey = "Authorization";
    
    /**
     * 令牌秘钥
     */
    private String secret;
    
    /**
     * 令牌有效期(60分钟)
     */
    private Integer expiration = 60;
    
    /**
     * 签发者
     */
    private String issuer = "zsheep";
    
    /**
     * 启用验证码
     */
    private Boolean captchaEnabled = true;
    
    private String captchaHost = "http://localhost";
    
    private String captchaPath = "/captcha";
    
    /**
     * 站点的密钥
     */
    private String captchaSecretKey = "";
    
    /**
     * 为其提供质询的主机名
     */
    private String hostname = "localhost";
}
