package com.zsheep.ai.domain.model;

import lombok.Data;

@Data
public class StableDiffusionModelVo {
    private String hash;
    
    private String modelName;
    
    private String modelImage;
    
    private String description;
}
