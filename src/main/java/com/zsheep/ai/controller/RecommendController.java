package com.zsheep.ai.controller;

import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.domain.model.Txt2ImgTaskVo;
import com.zsheep.ai.domain.model.UserWorkParamsVo;
import com.zsheep.ai.service.Txt2ImgImageService;
import com.zsheep.ai.service.Txt2ImgTaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/works/recommend")
public class RecommendController {
    
    @Resource
    private Txt2ImgTaskService txt2ImgTaskService;
    
    @Resource
    private Txt2ImgImageService txt2ImgImageService;
    
    @GetMapping("/txt2img")
    public R<?> getRecommendImages(UserWorkParamsVo params) {
        return R.ok(txt2ImgTaskService.getRecommendImages(params));
    }
    
    @GetMapping("/random")
    public R<?> getRandomImages(Double width, Double height, Double ratio) {
        return R.ok(txt2ImgImageService.getRandomImages(width, height, ratio));
    }
}
