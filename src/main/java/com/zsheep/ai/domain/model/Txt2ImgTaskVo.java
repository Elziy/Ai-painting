package com.zsheep.ai.domain.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Txt2ImgTaskVo {
    private String tid;
    
    private Long uid;
    
    private List<Txt2ImgImageVo> images;
    
    private Txt2ImgParamsVo parameters;
    
    private Date createTime;
}
