package com.zsheep.ai.domain.model;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

@Data
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
public class ControlNetArgs {
    private Boolean enabled = true;
    private String filter;
    private String module;
    private String model;
    private Double weight = 1.0;
    private ControlNetImage image;
    private ControlNetImage inputImage;
    private Integer resizeMode = 1;
    private Boolean lowvram = false;
    private Integer processorRes = 512;
    private Integer thresholdA = -1;
    private Integer thresholdB = -1;
    private Double guidanceStart = 0.0;
    private Double guidanceEnd = 1.0;
    private Integer controlMode = 0;
    private Boolean pixelPerfect = false;
    
    @Data
    public static class ControlNetImage {
        private Double width;
        
        private Double height;
        
        private String imageUrl;
    }
}
