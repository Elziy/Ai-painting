package com.zsheep.ai.common.security.service;

import com.zsheep.ai.common.constants.enums.UserStatus;
import com.zsheep.ai.common.core.domain.model.LoginUser;
import com.zsheep.ai.common.exception.user.UserException;
import com.zsheep.ai.common.exception.user.UserPasswordNotMatchException;
import com.zsheep.ai.domain.entity.User;
import com.zsheep.ai.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.Objects;

/**
 * 自定义的密码校验
 *
 * @author Elziy
 */
@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {
    
    @Resource
    private UserService userService;
    
    @Resource
    private PasswordService passwordService;
    
    /**
     * 得到数据库中登录用户的信息
     *
     * @param email 邮箱
     * @return {@link UserDetails}
     * @throws UsernameNotFoundException 用户名没有发现异常
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        int retryCount = passwordService.validatePasswordMaxRetryCount(email);
        User user = userService.selectUserByEmail(email);
        if (Objects.isNull(user)) {
            throw new UserPasswordNotMatchException();
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            throw new UserException("user.blocked", new Object[]{email});
        }
        passwordService.validate(user, retryCount);
        return createLoginUser(user);
    }
    
    public UserDetails createLoginUser(User user) {
        // TODO: 查找用户的权限
        return new LoginUser(user, new LinkedList<>());
    }
}
