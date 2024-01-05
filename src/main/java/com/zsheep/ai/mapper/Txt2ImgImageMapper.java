package com.zsheep.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsheep.ai.domain.entity.Txt2ImgImage;
import com.zsheep.ai.domain.model.ImageVo;
import com.zsheep.ai.domain.model.Txt2ImgImageVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 针对表【txt_2_img_image】的数据库操作Mapper
 *
 * @author Elziy
 */
public interface Txt2ImgImageMapper extends BaseMapper<Txt2ImgImage> {
    
    String selectTidByImageId(@Param("imageId") String imageId);
    
    /**
     * 批量插入图片
     *
     * @param images 图片
     * @return int
     */
    int insertBatch(@Param("images") List<Txt2ImgImage> images);
    
    /**
     * 删除属于自己的图像
     *
     * @param imageId 图像ID
     * @param uid     用户ID
     * @return int
     */
    int deleteImage(@Param("imageId") String imageId, @Param("uid") Long uid);
    
    /**
     * 设置图像为公开
     *
     * @param imageId 图像ID
     * @param uid     用户ID
     * @return int
     */
    int setPublic(@Param("imageId") String imageId, @Param("uid") Long uid);
    
    /**
     * 设置图像为私有
     *
     * @param imageId 图像ID
     * @param uid     用户ID
     * @return int
     */
    int setPrivate(@Param("imageId") String imageId, @Param("uid") Long uid);
    
    /**
     * 根据任务ID查询图片
     *
     * @param tid tid
     * @param uid 用户ID
     * @return {@link List}<{@link Txt2ImgImageVo}>
     */
    List<Txt2ImgImageVo> selectImagesByTid(@Param("tid") String tid, @Param("uid") Long uid);
    
    ImageVo selectRandomImages(@Param("width") Double width, @Param("height") Double height, @Param("wide") Boolean wide);
}




