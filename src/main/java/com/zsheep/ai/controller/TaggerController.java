package com.zsheep.ai.controller;

import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.domain.model.TaggerParamsVo;
import com.zsheep.ai.service.AiPaintingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TaggerController {
    @Resource
    private AiPaintingService aiPaintingService;
    
    @PostMapping("/tagger/tags")
    @PreAuthorize("@ss.hasRole('VIP')")
    public R<?> getTags(@RequestBody TaggerParamsVo params) {
        return R.ok(aiPaintingService.getTags(params));
    }
}
