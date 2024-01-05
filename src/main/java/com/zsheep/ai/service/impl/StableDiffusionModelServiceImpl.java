package com.zsheep.ai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsheep.ai.common.constants.CacheConstants;
import com.zsheep.ai.common.core.redis.RedisCache;
import com.zsheep.ai.domain.entity.StableDiffusionModel;
import com.zsheep.ai.domain.model.StableDiffusionModelVo;
import com.zsheep.ai.mapper.StableDiffusionModelMapper;
import com.zsheep.ai.service.StableDiffusionModelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 针对表【stable_diffusion_model】的数据库操作Service实现
 *
 * @author Elziy
 */
@Service
public class StableDiffusionModelServiceImpl extends ServiceImpl<StableDiffusionModelMapper, StableDiffusionModel>
        implements StableDiffusionModelService {
    
    @Resource
    private RedisCache redisCache;
    
    @Override
    public List<StableDiffusionModelVo> getAllModels() {
        return baseMapper.getAllModels();
    }
    
    @Override
    public List<StableDiffusionModelVo> getAllModelsFromRedis() {
        List<StableDiffusionModelVo> models = redisCache.getCacheObject(CacheConstants.STABLE_DIFFUSION_MODELS);
        if (models == null || models.isEmpty()) {
            models = getAllModels();
            redisCache.setCacheObject(CacheConstants.STABLE_DIFFUSION_MODELS, models);
        }
        return models;
    }
}




