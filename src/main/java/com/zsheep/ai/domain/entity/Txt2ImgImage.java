package com.zsheep.ai.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文字转图片的图片表
 *
 * @author Elziy
 */
@TableName(value ="txt_2_img_image")
@Data
public class Txt2ImgImage implements Serializable {
    @TableId
    private String imageId;

    private Double width;

    private Double height;

    private String imageUrl;
    
    private Long seed;
    
    private Boolean isPublic;
    
    @TableLogic
    private Integer delFlag;

    private Date createTime;
    
    private String tid;

    private static final long serialVersionUID = 1L;
}