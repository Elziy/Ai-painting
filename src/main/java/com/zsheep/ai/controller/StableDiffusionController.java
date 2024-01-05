package com.zsheep.ai.controller;

import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.service.StableDiffusionModelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class StableDiffusionController {
    
    @Resource
    private StableDiffusionModelService stableDiffusionModelService;
    
    @GetMapping("/sd/stable-diffusion-models")
    public R<?> getStableDiffusionModels() {
        return R.ok(stableDiffusionModelService.getAllModelsFromRedis());
    }
}
