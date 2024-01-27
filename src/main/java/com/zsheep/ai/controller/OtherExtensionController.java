package com.zsheep.ai.controller;

import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.service.OtherExtensionService;
import com.zsheep.ai.utils.MessageUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/extension")
public class OtherExtensionController {
    
    @Resource
    private OtherExtensionService otherExtensionService;
    
    @GetMapping("/after-detailer/models")
    public R<?> AdetailerModels() {
        try {
            return R.ok(otherExtensionService.getADetailerModels(), null);
        } catch (Exception e) {
            return R.error();
        }
    }
}
