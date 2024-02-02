package com.zsheep.ai.config;

import com.zsheep.ai.common.constants.CacheConstants;
import com.zsheep.ai.common.core.redis.RedisCache;
import com.zsheep.ai.domain.model.StableDiffusionModelVo;
import com.zsheep.ai.service.StableDiffusionModelService;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

// @Configuration
public class StableDiffusionConfig {
    
    @Resource
    StableDiffusionModelService stableDiffusionModelService;
    
    @Resource
    RedisCache redisCache;
    
    @PostConstruct
    public void loadStableDiffusionModelsToRedis() {
        List<StableDiffusionModelVo> models = stableDiffusionModelService.getAllModels();
        redisCache.setCacheObject(CacheConstants.STABLE_DIFFUSION_MODELS, models);
    }
    
}
