package com.zsheep.ai.common.exception.service;

import com.zsheep.ai.common.constants.HttpStatus;
import com.zsheep.ai.common.exception.base.BaseException;

/**
 * 服务异常
 *
 * @author Elziy
 */
public class ServiceException extends BaseException {
    private static final long serialVersionUID = 1L;
    
    private Integer errorCode;
    
    public ServiceException(String message) {
        super(null, null, message);
        this.errorCode = HttpStatus.ERROR;
    }
    
    public ServiceException(String code, Object[] args) {
        super(code, args, "系统服务异常");
        this.errorCode = HttpStatus.ERROR;
    }
    
    public ServiceException(String code, Object[] args, Integer errorCode) {
        super(code, args, "系统服务异常");
        this.errorCode = errorCode;
    }
    
    public Integer getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
