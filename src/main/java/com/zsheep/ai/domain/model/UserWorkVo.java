package com.zsheep.ai.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserWorkVo extends Txt2ImgTaskVo {
    /**
     * 作者用户名
     */
    private String username;
    
    /**
     * 作者头像
     */
    private String avatar;
    
    /**
     * 是否点赞
     */
    private Boolean isLike;
}
