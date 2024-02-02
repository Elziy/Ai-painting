package com.zsheep.ai.controller;

import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.common.exception.task.TaskException;
import com.zsheep.ai.domain.model.TaskProgressVo;
import com.zsheep.ai.domain.model.Txt2ImgParamsVo;
import com.zsheep.ai.service.AiPaintingService;
import com.zsheep.ai.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
public class AiPaintingController {
    
    public static final Txt2ImgParamsVo DEFAULT_TXT2IMG_PARAMS = new Txt2ImgParamsVo();
    
    @Resource
    private AiPaintingService aiPaintingService;
    
    @GetMapping("/txt2imgConfig")
    public R<?> txt2ImgConfig() {
        return R.ok(DEFAULT_TXT2IMG_PARAMS);
    }
    
    @GetMapping("/cancel/{taskId}")
    public R<?> cancel(@PathVariable String taskId) {
        try {
            Boolean cancel = aiPaintingService.cancelTask(taskId);
            if (cancel == null || !cancel) {
                return R.error(MessageUtils.message("task.cancel.error"));
            } else {
                return R.ok(MessageUtils.message("task.cancel.success"));
            }
        } catch (TaskException e) {
            return R.error(e.getMessage());
        } catch (Exception e) {
            return R.error(MessageUtils.message("task.cancel.error"));
        }
    }
    
    @PostMapping("/txt2img")
    public R<?> txt2Img2(@Validated @RequestBody Txt2ImgParamsVo params) {
        try {
            return R.ok(aiPaintingService.txt2imgAsync(params), MessageUtils.message("task.submit.success"));
        } catch (Exception e) {
            log.error(String.valueOf(e));
            return R.error(MessageUtils.message("task.submit.error"));
        }
    }
    
    @GetMapping("/progress/{taskId}")
    public R<?> progress(@PathVariable String taskId) {
        try {
            TaskProgressVo taskProgressVo = aiPaintingService.getTaskProgress(taskId);
            return R.ok(taskProgressVo);
        } catch (TaskException e) {
            return R.error(e.getMessage());
        }
    }
    
    @GetMapping("/txt2img/submittedTask")
    public R<?> txt2ImgTaskQueueOrRunning() {
        String taskId = aiPaintingService.getTaskIdByUserId();
        return R.ok(taskId, null);
    }
}
