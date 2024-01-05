package com.zsheep.ai.common.exception.base;

import cn.hutool.core.util.StrUtil;
import com.zsheep.ai.utils.MessageUtils;

/**
 * 基础异常
 *
 * @author ruoyi
 */
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    /**
     * 所属模块
     */
    private final String module;
    
    /**
     * 错误码
     */
    private final String messageCode;
    
    /**
     * 错误码对应的参数
     */
    private final Object[] args;
    
    /**
     * 错误消息
     */
    private final String defaultMessage;
    
    public BaseException(String module, String messageCode, Object[] args, String defaultMessage) {
        this.module = module;
        this.messageCode = messageCode;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }
    
    public BaseException(String module, String messageCode, Object[] args) {
        this(module, messageCode, args, null);
    }
    
    public BaseException(String module, String defaultMessage) {
        this(module, null, null, defaultMessage);
    }
    
    public BaseException(String messageCode, Object[] args) {
        this(null, messageCode, args, null);
    }
    
    public BaseException(String defaultMessage) {
        this(null, null, null, defaultMessage);
    }
    
    public BaseException(String messageCode, Object[] args, String defaultMessage) {
        this(null, messageCode, args, defaultMessage);
    }
    
    @Override
    public String getMessage() {
        String message = null;
        if (!StrUtil.isEmpty(messageCode)) {
            message = MessageUtils.message(messageCode, args);
        }
        if (message == null) {
            message = defaultMessage;
        }
        return message;
    }
    
    public String getModule() {
        return module;
    }
    
    public String getMessageCode() {
        return messageCode;
    }
    
    public Object[] getArgs() {
        return args;
    }
    
    public String getDefaultMessage() {
        return defaultMessage;
    }
}
