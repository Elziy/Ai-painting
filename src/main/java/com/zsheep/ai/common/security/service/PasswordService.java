package com.zsheep.ai.common.security.service;

import com.zsheep.ai.common.constants.CacheConstants;
import com.zsheep.ai.common.core.redis.RedisCache;
import com.zsheep.ai.common.exception.user.UserPasswordNotMatchException;
import com.zsheep.ai.common.exception.user.UserPasswordRetryLimitExceedException;
import com.zsheep.ai.common.security.context.AuthenticationContextHolder;
import com.zsheep.ai.config.auth.AuthProperties;
import com.zsheep.ai.domain.entity.User;
import com.zsheep.ai.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 登录密码方法
 *
 * @author Elziy
 */
@Slf4j
@Component
public class PasswordService {
    @Resource
    private RedisCache redisCache;
    
    @Resource
    private AuthProperties authProperties;
    
    /**
     * 登录账户密码错误次数缓存键名
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username) {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }
    
    /**
     * 验证密码最大重试次数
     *
     * @param username 用户名
     * @return int 重试次数
     * @throws UserPasswordRetryLimitExceedException 密码重试次数超限异常
     */
    public int validatePasswordMaxRetryCount(String username) {
        Integer retryCount = redisCache.getCacheObject(getCacheKey(username));
        
        if (retryCount == null) {
            retryCount = 0;
        }
        
        Integer passwordMaxRetryCount = authProperties.getPasswordMaxRetryCount();
        Integer passwordLockTime = authProperties.getPasswordLockTime();
        if (retryCount >= passwordMaxRetryCount) {
            log.error("用户:{} 登录失败次数超过{}次，账号已锁定{}分钟", username, passwordMaxRetryCount, passwordLockTime);
            throw new UserPasswordRetryLimitExceedException(passwordMaxRetryCount, passwordLockTime);
        }
        return retryCount;
    }
    
    /**
     * 验证密码,验证通过清除缓存,验证失败缓存重试次数
     *
     * @param user       用户
     * @param retryCount 重试计数
     * @throws UserPasswordNotMatchException 密码不匹配异常
     */
    public void validate(User user, int retryCount) {
        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String username = usernamePasswordAuthenticationToken.getName();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();
        
        Integer passwordMaxRetryCount = authProperties.getPasswordMaxRetryCount();
        Integer passwordLockTime = authProperties.getPasswordLockTime();
        
        if (!matches(user, password)) {
            retryCount = retryCount + 1;
            redisCache.setCacheObject(getCacheKey(username), retryCount, passwordLockTime, TimeUnit.MINUTES);
            log.error("用户:{} 登录失败，密码错误，剩余{}次机会", username, passwordMaxRetryCount - retryCount);
            throw new UserPasswordNotMatchException();
        } else {
            clearLoginRecordCache(username);
        }
    }
    
    public boolean matches(User user, String rawPassword) {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }
    
    /**
     * 清楚缓存登录记录
     *
     * @param username 登录账户
     */
    public void clearLoginRecordCache(String username) {
        if (redisCache.hasKey(getCacheKey(username))) {
            redisCache.deleteObject(getCacheKey(username));
        }
    }
}
