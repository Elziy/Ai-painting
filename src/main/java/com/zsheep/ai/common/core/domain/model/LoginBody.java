package com.zsheep.ai.common.core.domain.model;

/**
 * 用户登录对象
 *
 * @author Elziy
 */
public class LoginBody {
    /**
     * 用户名
     */
    private String email;
    
    /**
     * 用户密码
     */
    private String password;
    
    /**
     * 验证码
     */
    private String code;
    
    /**
     * 唯一标识
     */
    private String uuid;
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getUuid() {
        return uuid;
    }
    
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
