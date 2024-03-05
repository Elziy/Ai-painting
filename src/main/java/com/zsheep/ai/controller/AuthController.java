package com.zsheep.ai.controller;

import com.zsheep.ai.common.constants.HttpStatus;
import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.common.core.domain.model.LoginBody;
import com.zsheep.ai.common.core.domain.model.LoginUser;
import com.zsheep.ai.common.core.domain.model.RegisterBody;
import com.zsheep.ai.common.exception.service.ServiceException;
import com.zsheep.ai.common.security.service.LoginService;
import com.zsheep.ai.common.security.service.RegisterService;
import com.zsheep.ai.utils.MessageUtils;
import com.zsheep.ai.utils.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Resource
    private LoginService loginService;
    
    @Resource
    private RegisterService registerService;
    
    @PostMapping("/login")
    public R<?> login(@RequestBody LoginBody loginBody) {
        LoginUser loginUser = null;
        try {
            loginUser = loginService.login(loginBody.getEmail(), loginBody.getPassword(), loginBody.getCode(), loginBody.getUuid());
        } catch (ServiceException e) {
            return R.error(e.getMessage());
        } catch (Exception e) {
            return R.error(HttpStatus.UNAUTHORIZED, MessageUtils.error());
        }
        return R.ok(loginUser);
    }
    
    @GetMapping("/user")
    public R<?> getUserInfo() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        loginUser.setSession(null);
        loginUser.getUser().setPassword(null);
        loginUser.getUser().setRoles(null);
        return R.ok(loginUser);
    }
    
    @PostMapping("/register")
    public R<?> register(@RequestBody RegisterBody registerBody) {
        try {
            registerService.register(registerBody);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
        return R.ok();
    }
    
    
    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('A')")
    public LoginUser hello() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        return loginUser;
    }
    
    @GetMapping("/hello2")
    @PreAuthorize("@ss.hasRole('A')")
    public LoginUser hello2() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        return loginUser;
    }
    
    @GetMapping("/hello3")
    public R<?> hello3() {
        return R.error(MessageUtils.error());
    }
}
