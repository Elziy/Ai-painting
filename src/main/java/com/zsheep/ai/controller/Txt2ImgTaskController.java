package com.zsheep.ai.controller;

import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.domain.model.UserWorkParamsVo;
import com.zsheep.ai.domain.model.Txt2ImgTaskVo;
import com.zsheep.ai.service.Txt2ImgTaskService;
import com.zsheep.ai.service.UserLikeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/works/txt2img")
public class Txt2ImgTaskController {
    @Resource
    private Txt2ImgTaskService txt2ImgTaskService;
    
    @Resource
    private UserLikeService userLikeService;
    
    @GetMapping("/{tid}")
    public R<Txt2ImgTaskVo> txt2Img(@PathVariable String tid) {
        return R.ok(txt2ImgTaskService.getUserWorkByTid(tid));
    }
    
    @GetMapping("/all/{uid}")
    public R<List<Txt2ImgTaskVo>> txt2Img(UserWorkParamsVo params, @PathVariable Long uid) {
        return R.ok(txt2ImgTaskService.getTxt2ImgTaskVoByUid(uid, params));
    }
    
    @PostMapping("/like/{tid}")
    public R<?> likeTxt2ImgTask(@PathVariable String tid) {
        userLikeService.likeTxt2ImgTask(tid);
        return R.ok();
    }
    
    @PostMapping("/dislike/{tid}")
    public R<?> dislikeTxt2ImgTask(@PathVariable String tid) {
        userLikeService.dislikeTxt2ImgTask(tid);
        return R.ok();
    }
    
}
