package com.zsheep.ai.common.core.domain;

import com.zsheep.ai.common.constants.HttpStatus;
import com.zsheep.ai.utils.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @author Elziy
 */
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 成功
     */
    public static final int SUCCESS = HttpStatus.SUCCESS;
    
    /**
     * 失败
     */
    public static final int FAIL = HttpStatus.ERROR;
    
    private int code;
    
    private String message;
    
    private T data;
    
    public static <T> R<T> ok() {
        String message = getSuccessMessage();
        return restResult(null, SUCCESS, message);
    }
    
    public static <T> R<T> ok(String message) {
        return restResult(null, SUCCESS, message);
    }
    
    public static <T> R<T> ok(T data) {
        String message = getSuccessMessage();
        return restResult(data, SUCCESS, message);
    }
    
    public static <T> R<T> ok(T data, String msg) {
        if (StringUtils.isEmpty(msg)) {
            return ok(data);
        }
        return restResult(data, SUCCESS, msg);
    }
    
    public static <T> R<T> error() {
        String message = getErrorMessage();
        return restResult(null, FAIL, message);
    }
    
    public static <T> R<T> error(String msg) {
        return restResult(null, FAIL, msg);
    }
    
    public static <T> R<T> error(T data) {
        String message = getErrorMessage();
        return restResult(data, FAIL, message);
    }
    
    public static <T> R<T> error(T data, String msg) {
        if (StringUtils.isEmpty(msg)) {
            return error(data);
        }
        return restResult(data, FAIL, msg);
    }
    
    public static <T> R<T> error(int code, String msg) {
        return restResult(null, code, msg);
    }
    
    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMessage(msg);
        return r;
    }
    
    public int getCode() {
        return code;
    }
    
    public void setCode(int code) {
        this.code = code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    private static String getSuccessMessage() {
        String message;
        if (StringUtils.equals(LocaleContextHolder.getLocale().getLanguage(), "zh")) {
            message = "成功";
        } else {
            message = "success";
        }
        return message;
    }
    
    private static String getErrorMessage() {
        String message;
        if (StringUtils.equals(LocaleContextHolder.getLocale().getLanguage(), "zh")) {
            message = "失败";
        } else {
            message = "error";
        }
        return message;
    }
}
