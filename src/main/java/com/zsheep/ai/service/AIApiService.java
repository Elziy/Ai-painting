package com.zsheep.ai.service;

import com.zsheep.ai.domain.entity.StableDiffusionModel;
import com.zsheep.ai.domain.model.ControlNetPreprocessParamsVo;
import com.zsheep.ai.domain.model.TaggerParamsVo;
import com.zsheep.ai.domain.model.Txt2ImgParamsVo;
import com.zsheep.ai.domain.model.api.ApiControlNetPreprocessResponse;
import com.zsheep.ai.domain.model.api.ApiImgageResponse;
import com.zsheep.ai.domain.model.api.ApiTaggerResponse;
import com.zsheep.ai.domain.model.api.ApiTaskProgress;

import java.util.List;

public interface AIApiService {
    ApiImgageResponse getImageByApi(Txt2ImgParamsVo params);
    
    ApiTaskProgress getTaskProgressByApi();
    
    ApiTaggerResponse getTagsByApi(TaggerParamsVo params);
    
    List<StableDiffusionModel> getStableDiffusionModelsByApi();
    
    ApiControlNetPreprocessResponse getControlNetPreprocessByApi(ControlNetPreprocessParamsVo params);
}
