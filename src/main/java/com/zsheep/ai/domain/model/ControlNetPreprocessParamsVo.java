package com.zsheep.ai.domain.model;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

@Data
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
public class ControlNetPreprocessParamsVo {
    private InputImages inputImages;
    private String module;
    private int pres;
    private int pthrA;
    private int pthrB;
    private int wide;
    private int height;
    private boolean pixelPerfect;
    private String removeMask;
    
    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    static class InputImages {
        
        private String image;
        private String mask;
    }
}
