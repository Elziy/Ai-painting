package com.zsheep.ai.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

@Data
public class TagsVo {
    /**
     * 分级
     */
    private String rating;
    
    /**
     * 标签
     */
    private String tags;
    
    /**
     * 一般标签
     */
    private LinkedHashMap<String, BigDecimal> generalTags;
    
    /**
     * 角色标签
     */
    private LinkedHashMap<String, BigDecimal> characterTags;
}
