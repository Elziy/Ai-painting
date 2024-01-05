package com.zsheep.ai.domain.model;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

/**
 * 标签
 *
 * @author Elziy
 */
@Data
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
public class TaggerParamsVo {
    private String image;
    
    private String model;
    
    private Double threshold = 0.35;
    
    private Boolean unloadModelAfterRunning = true;
    
    private Boolean replaceUnderscore = true;
}
