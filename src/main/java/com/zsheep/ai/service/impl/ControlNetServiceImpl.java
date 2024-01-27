package com.zsheep.ai.service.impl;

import com.zsheep.ai.domain.model.ControlNetPreprocessParamsVo;
import com.zsheep.ai.domain.model.api.ApiControlNetPreprocessResponse;
import com.zsheep.ai.service.AIApiService;
import com.zsheep.ai.service.ControlNetService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.zsheep.ai.common.constants.CacheConstants.ControlNetTypes;

@Service
public class ControlNetServiceImpl implements ControlNetService {
    
    @Resource
    private AIApiService aiApiService;
    
    @Override
    public ApiControlNetPreprocessResponse getControlNetPreprocess(ControlNetPreprocessParamsVo params) {
        ApiControlNetPreprocessResponse preprocess = aiApiService.getControlNetPreprocessByApi(params);
        preprocess.setImage("data:image/jpeg;base64," + preprocess.getImage());
        return preprocess;
    }
    
    @Override
    @CacheEvict(value = ControlNetTypes)
    public String getControlNetType() {
        return aiApiService.getControlNetTypeByApi();
    }
}
