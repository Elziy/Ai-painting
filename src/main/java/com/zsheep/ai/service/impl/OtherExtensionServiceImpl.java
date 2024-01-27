package com.zsheep.ai.service.impl;

import com.zsheep.ai.service.AIApiService;
import com.zsheep.ai.service.OtherExtensionService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.zsheep.ai.common.constants.CacheConstants.AfterDetailerModels;

@Service
public class OtherExtensionServiceImpl implements OtherExtensionService {
    @Resource
    private AIApiService aiApiService;
    
    @Override
    @CacheEvict(value = AfterDetailerModels, allEntries = true)
    public String getADetailerModels() {
        return aiApiService.getADetailerModels();
    }
}
