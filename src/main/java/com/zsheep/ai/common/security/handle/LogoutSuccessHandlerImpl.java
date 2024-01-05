package com.zsheep.ai.common.security.handle;

import com.alibaba.fastjson2.JSON;
import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.common.core.domain.model.LoginUser;
import com.zsheep.ai.common.security.service.TokenService;
import com.zsheep.ai.utils.MessageUtils;
import com.zsheep.ai.utils.ServletUtils;
import com.zsheep.ai.utils.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义退出处理类 返回成功
 *
 * @author Elziy
 */
@Component("logoutSuccessHandler")
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Resource
    private TokenService tokenService;
    
    /**
     * 退出处理
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            tokenService.delLoginUser(loginUser.getUserId(), loginUser.getSession());
        }
        String message = MessageUtils.message("user.logout.success");
        ServletUtils.renderString(response, JSON.toJSONString(R.ok(null, message)));
    }
}
