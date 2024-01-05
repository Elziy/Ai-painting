package com.zsheep.ai.controller;

import com.zsheep.ai.common.constants.HttpStatus;
import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.service.Txt2ImgImageService;
import com.zsheep.ai.utils.MessageUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/works/txt2img")
public class Txt2ImgImageController {
    
    @Resource
    private Txt2ImgImageService txt2ImgImageService;
    
    @DeleteMapping("/{imageId}")
    public R<?> deleteImage(@PathVariable String imageId) {
        boolean deleteImage = txt2ImgImageService.deleteImage(imageId);
        return deleteImage ? R.ok(MessageUtils.message("delete.success"))
                : R.error(HttpStatus.FORBIDDEN, MessageUtils.message("delete.error"));
    }
    
    @PutMapping("/2public/{imageId}")
    public R<?> setPublic(@PathVariable String imageId) {
        boolean setPublic = txt2ImgImageService.setPublic(imageId);
        return setPublic ? R.ok(MessageUtils.message("set.success"))
                : R.error(HttpStatus.FORBIDDEN, MessageUtils.message("set.error"));
    }
    
    @PutMapping("/2private/{imageId}")
    public R<?> setPrivate(@PathVariable String imageId) {
        boolean setPublic = txt2ImgImageService.setPrivate(imageId);
        return setPublic ? R.ok(MessageUtils.message("set.success"))
                : R.error(HttpStatus.FORBIDDEN, MessageUtils.message("set.error"));
    }
}
