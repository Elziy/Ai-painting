package com.zsheep.ai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsheep.ai.common.constants.CacheConstants;
import com.zsheep.ai.common.core.redis.RedisCache;
import com.zsheep.ai.domain.entity.Txt2ImgImage;
import com.zsheep.ai.domain.model.ImageVo;
import com.zsheep.ai.domain.model.Txt2ImgImageVo;
import com.zsheep.ai.mapper.Txt2ImgImageMapper;
import com.zsheep.ai.service.Txt2ImgImageService;
import com.zsheep.ai.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 针对表【txt_2_img_image】的数据库操作Service实现
 *
 * @author Elziy
 */
@Service
public class Txt2ImgImageServiceImpl extends ServiceImpl<Txt2ImgImageMapper, Txt2ImgImage>
        implements Txt2ImgImageService {
    
    @Resource
    private RedisCache redisCache;
    
    @Override
    public String getTidByImageId(String imageId) {
        String tidFromRedis = redisCache.getCacheObject(CacheConstants.IMAGE_TID + imageId);
        if (tidFromRedis != null) {
            return tidFromRedis;
        }
        String tid = this.baseMapper.selectTidByImageId(imageId);
        redisCache.setCacheObject(CacheConstants.IMAGE_TID + imageId, tid);
        return tid;
    }
    
    @Override
    public boolean insertBatch(List<Txt2ImgImage> images) {
        if (images == null || images.isEmpty()) {
            return false;
        }
        int i = baseMapper.insertBatch(images);
        return i == images.size();
    }
    
    @Override
    public boolean deleteImage(String imageId) {
        Long uid = SecurityUtils.getUserId();
        int deleteImage = this.baseMapper.deleteImage(imageId, uid);
        if (deleteImage == 1) {
            redisCache.deleteObject(CacheConstants.TXT_2_IMG_WORK_KEY + getTidByImageId(imageId));
        }
        return deleteImage == 1;
    }
    
    @Override
    public boolean setPublic(String imageId) {
        Long uid = SecurityUtils.getUserId();
        int setPublic = this.baseMapper.setPublic(imageId, uid);
        if (setPublic == 1) {
            redisCache.deleteObject(CacheConstants.TXT_2_IMG_WORK_KEY + getTidByImageId(imageId));
        }
        return setPublic == 1;
    }
    
    @Override
    public boolean setPrivate(String imageId) {
        Long uid = SecurityUtils.getUserId();
        int setPublic = this.baseMapper.setPrivate(imageId, uid);
        if (setPublic == 1) {
            redisCache.deleteObject(CacheConstants.TXT_2_IMG_WORK_KEY + getTidByImageId(imageId));
        }
        return setPublic == 1;
    }
    
    @Override
    public List<Txt2ImgImageVo> getImagesByTid(String tid, Long uid) {
        return this.baseMapper.selectImagesByTid(tid, uid);
    }
    
    @Override
    public ImageVo getRandomImages(Double width, Double height, Double ratio) {
        return this.baseMapper.selectRandomImages(width, height, ratio.compareTo(1.0) > 0);
    }
}