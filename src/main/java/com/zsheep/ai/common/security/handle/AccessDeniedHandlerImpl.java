package com.zsheep.ai.common.security.handle;

import com.alibaba.fastjson2.JSON;
import com.zsheep.ai.common.constants.HttpStatus;
import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.utils.MessageUtils;
import com.zsheep.ai.utils.SecurityUtils;
import com.zsheep.ai.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component("accessDeniedHandler")
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        int code = HttpStatus.FORBIDDEN;
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限校验失败,访问者 :{}", requestURI, SecurityUtils.getUserId());
        String message = MessageUtils.message("system.no.permission");
        ServletUtils.renderString(response, JSON.toJSONString(R.error(code, message)));
    }
}
