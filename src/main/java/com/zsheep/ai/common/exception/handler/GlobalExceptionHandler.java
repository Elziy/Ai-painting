package com.zsheep.ai.common.exception.handler;

import com.zsheep.ai.common.constants.HttpStatus;
import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.common.exception.service.ServiceException;
import com.zsheep.ai.utils.SecurityUtils;
import com.zsheep.ai.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 *
 * @author Elziy
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    /**
     * 权限校验异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public R<?> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限校验失败,访问者:{}", requestURI, SecurityUtils.getUserId());
        String message;
        if (StringUtils.equals(LocaleContextHolder.getLocale().getLanguage(), "zh")) {
            message = "对不起,您没有权限访问该资源";
        } else {
            message = "Sorry, you don't have permission to access this resource";
        }
        return R.error(HttpStatus.FORBIDDEN, message);
    }
    
    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<?> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                    HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求,,访问者:{}", requestURI, e.getMethod(), SecurityUtils.getUserId());
        String message;
        if (StringUtils.equals(LocaleContextHolder.getLocale().getLanguage(), "zh")) {
            message = "不支持" + e.getMethod() + "请求";
        } else {
            message = "Request method '" + e.getMethod() + "' not supported";
        }
        return R.error(HttpStatus.BAD_METHOD, message);
    }
    
    /**
     * 处理请求参数缺失
     *
     * @param request 请求
     * @return {@link R}<?>
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public R<?> handleMissingServletRequestParameterException(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',请求缺少参数", requestURI);
        String message;
        if (StringUtils.equals(LocaleContextHolder.getLocale().getLanguage(), "zh")) {
            message = "请求缺少参数";
        } else {
            message = "Request missing parameter";
        }
        return R.error(HttpStatus.BAD_REQUEST, message);
    }
    
    /**
     * 处理http消息不可读异常
     *
     * @param e       e
     * @param request 请求
     * @return {@link R}
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',请求消息不可读,访问者:{}", requestURI, SecurityUtils.getUserId());
        String message;
        if (StringUtils.equals(LocaleContextHolder.getLocale().getLanguage(), "zh")) {
            message = "请求消息不可读";
        } else {
            message = "Http message not readable";
        }
        return R.error(HttpStatus.BAD_REQUEST, message);
    }
    
    /**
     * 处理参数校验异常
     *
     * @param e       异常
     * @param request 请求
     * @return {@link R}
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error("请求地址'{}',发生参数校验异常:{},访问者:{}", requestURI, message, SecurityUtils.getUserId());
        return R.error(message);
    }
    
    /**
     * 处理参数绑定异常
     *
     * @param e       异常
     * @param request 请求
     * @return {@link R}
     */
    @ExceptionHandler({BindException.class, MethodArgumentTypeMismatchException.class})
    public R<?> handleBindException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String err;
        if (e instanceof BindException) {
            err = ((BindException) e).getBindingResult().getAllErrors().get(0).getDefaultMessage();
        } else {
            err = e.getMessage();
        }
        String message;
        if (StringUtils.equals(LocaleContextHolder.getLocale().getLanguage(), "zh")) {
            message = "参数有误,请检查您的参数";
        } else {
            message = "Parameter error, please check your parameters";
        }
        log.error("请求地址'{}',发生参数绑定异常:{},访问者:{}", requestURI, err, SecurityUtils.getUserId());
        return R.error(message);
    }
    
    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public R<?> handleServiceException(ServiceException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生业务异常:{}", requestURI, e.getMessage());
        Integer code = e.getErrorCode();
        return StringUtils.isNotNull(code) ?
                R.error(code.intValue(), e.getMessage())
                : R.error(e.getMessage());
    }
    
    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public R<?> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知运行异常:{}", requestURI, e.getMessage());
        e.printStackTrace();
        String message;
        if (StringUtils.equals(LocaleContextHolder.getLocale().getLanguage(), "zh")) {
            message = "系统出现未知异常,请稍后再试";
        } else {
            message = "An unknown exception occurred. Please try again later";
        }
        return R.error(message);
    }
    
    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public R<?> handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常:{}", requestURI, e.getMessage());
        e.printStackTrace();
        String message;
        if (StringUtils.equals(LocaleContextHolder.getLocale().getLanguage(), "zh")) {
            message = "系统服务异常,请稍后再试";
        } else {
            message = "The system service is abnormal. Please try again later";
        }
        return R.error(message);
    }
}
