package com.zsheep.ai.service;

import com.zsheep.ai.domain.model.ControlNetPreprocessParamsVo;
import com.zsheep.ai.domain.model.api.ApiControlNetPreprocessResponse;

public interface ControlNetService {
    ApiControlNetPreprocessResponse getControlNetPreprocess(ControlNetPreprocessParamsVo params);
    
    String getControlNetType();
}
