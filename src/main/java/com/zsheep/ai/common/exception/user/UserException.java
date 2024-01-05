package com.zsheep.ai.common.exception.user;

import com.zsheep.ai.common.exception.base.BaseException;

/**
 * 用户信息异常类
 *
 * @author Elziy
 */
public class UserException extends BaseException {
    private static final long serialVersionUID = 1L;
    
    public UserException(String code, Object[] args) {
        super("user", code, args, "用户信息异常");
    }
}
