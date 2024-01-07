package com.zsheep.ai.config.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ai.upload")
public class UploadProperties {
    
    private String uploadHost = "http://127.0.0.1";
    
    private String imageHost = "http://127.0.0.1";
    
}
