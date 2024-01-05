package com.zsheep.ai.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class UserInfoVo {
    private Long uid;
    
    private String username;
    
    private String avatar;
    
    private List<Txt2ImgTaskVo> works;
    
    private List<Txt2ImgTaskVo> likes;
}
