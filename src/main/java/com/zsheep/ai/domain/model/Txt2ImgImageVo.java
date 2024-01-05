package com.zsheep.ai.domain.model;

import lombok.Data;

import java.util.Date;

@Data
public class Txt2ImgImageVo {
    private String imageId;
    
    private Double width;
    
    private Double height;
    
    private String imageUrl;
    
    private Long seed;
    
    private Boolean isPublic;
    
    private Date createTime;
}
