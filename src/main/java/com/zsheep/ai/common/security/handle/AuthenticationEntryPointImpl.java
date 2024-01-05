package com.zsheep.ai.common.security.handle;

import com.alibaba.fastjson2.JSON;
import com.zsheep.ai.common.constants.HttpStatus;
import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.utils.ServletUtils;
import com.zsheep.ai.utils.StringUtils;
import com.zsheep.ai.utils.ip.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 *
 * @author Elziy
 */
@Slf4j
@Component("authenticationEntryPoint")
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        int code = HttpStatus.UNAUTHORIZED;
        String requestURI = request.getRequestURI();
        String ipAddr = IpUtils.getIpAddr(request);
        log.error("请求地址'{}',认证失败,访问ip:{}", requestURI, ipAddr);
        String message;
        if (StringUtils.equals(LocaleContextHolder.getLocale().getLanguage(), "zh")) {
            message = "对不起,您还没有登录";
        } else {
            message = "Sorry, you need to login to access this resource";
        }
        ServletUtils.renderString(response, JSON.toJSONString(R.error(code, message)));
    }
}
