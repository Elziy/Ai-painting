package com.zsheep.ai.config.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ai.upload")
public class UploadProperties {
    
    private String uploadHost = "";
    
    private String imageHost = "";
    
}
