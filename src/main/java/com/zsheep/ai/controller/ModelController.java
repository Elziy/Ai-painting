package com.zsheep.ai.controller;

import com.zsheep.ai.common.controller.BaseController;
import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.service.ModelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 模型Controller
 *
 * @author elziy6
 */
@RestController
public class ModelController extends BaseController {
    @Resource
    private ModelService modelService;
    
    @GetMapping("/sd/models")
    public R<?> list() {
        return R.ok(modelService.listTree());
    }
    
    @GetMapping("/sd/models/{hash}")
    public R<?> get(@PathVariable String hash) {
        return R.ok(modelService.getOneByHash(hash));
    }
}
