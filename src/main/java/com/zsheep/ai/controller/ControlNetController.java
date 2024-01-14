package com.zsheep.ai.controller;

import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.domain.model.ControlNetPreprocessParamsVo;
import com.zsheep.ai.service.ControlNetService;
import com.zsheep.ai.utils.MessageUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ControlNetController {
    
    @Resource
    private ControlNetService controlNetService;
    
    @PostMapping("/controlnet/preprocess")
    public R<?> preprocess(@Validated @RequestBody ControlNetPreprocessParamsVo params) {
        try {
            return R.ok(controlNetService.getControlNetPreprocess(params));
        } catch (Exception e) {
            return R.error(MessageUtils.message("controlnet.is.error"));
        }
    }
    
    @GetMapping("/controlnet/type")
    public R<?> type() {
        try {
            return R.ok(controlNetService.getControlNetType(), null);
        } catch (Exception e) {
            return R.error(MessageUtils.message("controlnet.is.error"));
        }
    }
}
