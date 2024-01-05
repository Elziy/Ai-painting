package com.zsheep.ai.common.security.service;

import com.alibaba.fastjson2.JSON;
import com.zsheep.ai.common.constants.HttpMethods;
import com.zsheep.ai.common.constants.HttpStatus;
import com.zsheep.ai.common.core.domain.model.LoginUser;
import com.zsheep.ai.common.core.domain.model.TurnstileRequest;
import com.zsheep.ai.common.core.domain.model.TurnstileResponse;
import com.zsheep.ai.common.exception.service.ServiceException;
import com.zsheep.ai.common.exception.user.UserException;
import com.zsheep.ai.common.exception.user.UserPasswordNotMatchException;
import com.zsheep.ai.common.security.context.AuthenticationContextHolder;
import com.zsheep.ai.config.api.ApiProperties;
import com.zsheep.ai.config.auth.AuthProperties;
import com.zsheep.ai.domain.entity.User;
import com.zsheep.ai.service.UserService;
import com.zsheep.ai.utils.DateUtils;
import com.zsheep.ai.utils.HttpUtil;
import com.zsheep.ai.utils.ServletUtils;
import com.zsheep.ai.utils.StringUtils;
import com.zsheep.ai.utils.ip.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class LoginService {
    
    @Resource
    private UserService userService;
    
    @Resource
    private AuthenticationManager authenticationManager;
    
    @Resource
    private TokenService tokenService;
    
    @Resource
    private AuthProperties authProperties;
    
    @Resource
    private ApiProperties apiProperties;
    
    /**
     * 登录
     *
     * @param email    邮箱
     * @param password 密码
     * @param code     验证码
     * @param uuid     uuid
     * @return {@link LoginUser}
     * @throws RuntimeException              密码错误或重试次数超限
     * @throws UserPasswordNotMatchException 密码不匹配
     * @throws ServiceException              服务异常
     */
    public LoginUser login(String email, String password, String code, String uuid) {
        // 验证码开关
        if (authProperties.getCaptchaEnabled()) {
            validateCode(code);
        }
        // 用户验证
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof InternalAuthenticationServiceException) {
                throw new RuntimeException(e.getMessage());
            } else if (e instanceof BadCredentialsException) {
                throw new UserPasswordNotMatchException();
            } else {
                log.error("登录异常:{}", e.getMessage());
                e.printStackTrace();
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUserId());
        return tokenService.createToken(loginUser);
    }
    
    /**
     * 校验验证码
     *
     * @param code 验证码
     */
    public void validateCode(String code) {
        if (StringUtils.isEmpty(code)) {
            throw new UserException("user.captcha.required", null);
        }
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64");
        headers.put("Connection", "keep-alive");
        Map<String, String> querys = new HashMap<>();
        TurnstileRequest turnstileRequest = new TurnstileRequest();
        turnstileRequest.setSecret(authProperties.getCaptchaSecretKey());
        turnstileRequest.setResponse(code);
        turnstileRequest.setRemoteip(IpUtils.getIpAddr(ServletUtils.getRequest()));
        String bodys = JSON.toJSONString(turnstileRequest);
        try {
            HttpResponse httpResponse = HttpUtil.doPost(authProperties.getCaptchaHost(), authProperties.getCaptchaPath(), HttpMethods.POST, headers, querys, bodys, apiProperties.getProxyEnable());
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SUCCESS) {
                String json = EntityUtils.toString(httpResponse.getEntity());
                TurnstileResponse turnstileResponse = JSON.parseObject(json, TurnstileResponse.class);
                if (!turnstileResponse.getSuccess()) {
                    throw new UserException("user.captcha.error", null);
                }
                if (!authProperties.getHostname().equals(turnstileResponse.getHostname())) {
                    throw new UserException("user.captcha.error", null);
                }
            } else {
                throw new UserException("user.captcha.error", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserException("user.captcha.error", null);
        }
    }
    
    /**
     * 记录登录信息
     *
     * @param uid 用户ID
     */
    public void recordLoginInfo(Long uid) {
        User user = new User();
        user.setId(uid);
        user.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        user.setLoginTime(DateUtils.now());
        userService.updateById(user);
    }
}
