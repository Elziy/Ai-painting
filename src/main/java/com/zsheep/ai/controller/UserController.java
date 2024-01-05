package com.zsheep.ai.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsheep.ai.common.controller.BaseController;
import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.domain.model.UserWorkParamsVo;
import com.zsheep.ai.domain.model.UserWorkVo;
import com.zsheep.ai.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    
    @Resource
    private UserService userService;
    
    @GetMapping("/{uid}")
    public R<?> txt2Img(UserWorkParamsVo params, @PathVariable Long uid) {
        return R.ok(userService.getUserInfoVoByUid(params, uid));
    }
    
    @GetMapping("/{uid}/works")
    public R<?> getUserWorks(UserWorkParamsVo params, @PathVariable Long uid) {
        Page<UserWorkVo> page = getPage();
        return R.ok(userService.getUserWorks(page, params, uid));
    }
    
    @GetMapping("/{uid}/likes")
    public R<?> getUserLikes(UserWorkParamsVo params, @PathVariable Long uid) {
        Page<UserWorkVo> page = getPage();
        return R.ok(userService.getUserLikes(page, params, uid));
    }
}
