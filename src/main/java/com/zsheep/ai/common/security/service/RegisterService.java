package com.zsheep.ai.common.security.service;

import com.zsheep.ai.common.core.domain.model.RegisterBody;
import com.zsheep.ai.common.exception.user.UserException;
import com.zsheep.ai.config.auth.AuthProperties;
import com.zsheep.ai.domain.entity.User;
import com.zsheep.ai.service.UserService;
import com.zsheep.ai.utils.DateUtils;
import com.zsheep.ai.utils.SecurityUtils;
import com.zsheep.ai.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RegisterService {
    @Resource
    private UserService userService;
    
    @Resource
    private LoginService loginService;
    
    @Resource
    private AuthProperties authProperties;
    
    public void register(RegisterBody registerBody) {
        String email = registerBody.getEmail(), password = registerBody.getPassword();
        User user = new User();
        user.setEmail(email);
        
        // 验证码开关
        if (authProperties.getCaptchaEnabled()) {
            validateCaptcha(registerBody.getCode());
        }
        
        if (StringUtils.isEmpty(email)) {
            throw new UserException("user.email.not.valid", new String[]{email});
        } else if (StringUtils.isEmpty(password)) {
            throw new UserException("user.password.not.valid", null);
        } else if (!userService.checkEmailUnique(email)) {
            throw new UserException("user.email.not.unique", new String[]{email});
        } else {
            user.setPassword(SecurityUtils.encryptPassword(password));
            user.setCreateTime(DateUtils.now());
            user.setUsername(email.substring(0, email.indexOf("@")));
            user.setAvatar(authProperties.getDefaultAvatar());
            boolean regFlag = userService.save(user);
            if (!regFlag) {
                throw new UserException("user.register.failed", null);
            }
        }
    }
    
    /**
     * 校验验证码
     *
     * @param code 验证码
     */
    public void validateCaptcha(String code) {
        loginService.validateCode(code);
    }
}
