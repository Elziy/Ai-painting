package com.zsheep.ai.mapper;

import com.zsheep.ai.domain.entity.StableDiffusionModel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsheep.ai.domain.model.StableDiffusionModelVo;

import java.util.List;

/**
 * 针对表【stable_diffusion_model】的数据库操作Mapper
 *
 * @author Elziy
 */
public interface StableDiffusionModelMapper extends BaseMapper<StableDiffusionModel> {
    
    List<StableDiffusionModelVo> getAllModels();
}




