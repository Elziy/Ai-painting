package com.zsheep.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zsheep.ai.domain.entity.Txt2ImgImage;
import com.zsheep.ai.domain.model.ImageVo;
import com.zsheep.ai.domain.model.Txt2ImgImageVo;

import java.util.List;

/**
 * 针对表【txt_2_img_image】的数据库操作Service
 *
 * @author Elziy
 */
public interface Txt2ImgImageService extends IService<Txt2ImgImage> {
    
    String getTidByImageId(String imageId);
    
    boolean insertBatch(List<Txt2ImgImage> images);
    
    boolean deleteImage(String imageId);
    
    boolean setPublic(String imageId);
    
    boolean setPrivate(String imageId);
    
    /**
     * 根据任务ID查询图片
     *
     * @param tid tid
     * @param uid 用户ID
     * @return {@link List}<{@link Txt2ImgImageVo}>
     */
    List<Txt2ImgImageVo> getImagesByTid(String tid, Long uid);
    
    /**
     * 获取随机图片
     *
     * @return {@link ImageVo}
     */
    ImageVo getRandomImages(Double width, Double height, Double ratio);
}
