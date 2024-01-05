package com.zsheep.ai.config.api;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ai.api")
public class ApiProperties {
    
    /**
     * 代理开关
     */
    private Boolean proxyEnable = false;
    
    /**
     * 代理主机
     */
    private String proxyHost = "127.0.0.1";
    
    /**
     * 代理端口
     */
    private Integer proxyPort = 7890;
    
    /**
     * ai服务地址
     */
    private String host;
    
    private String fileNameDateFormat = "yyyy/MM/dd";
    
    private String filePath = "D:images";
    
    /**
     * 图片后缀名
     */
    private String fileType = "png";
    
    /**
     * 完成任务缓存大小
     */
    private Integer finishTaskMapSize = 100;
}
