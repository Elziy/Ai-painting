package com.zsheep.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsheep.ai.domain.entity.StableDiffusionModel;
import com.zsheep.ai.domain.model.StableDiffusionModelVo;

import java.util.List;

/**
 * 针对表【stable_diffusion_model】的数据库操作Service
 *
 * @author Elziy
 */
public interface StableDiffusionModelService extends IService<StableDiffusionModel> {
    
    List<StableDiffusionModelVo> getAllModels();
    
    List<StableDiffusionModelVo> getAllModelsFromRedis();
}
