package com.zsheep.ai.domain.model.api;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

@Data
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
public class ApiTaskProgress {
    private String taskId;
    
    private Double progress = 0.0;
    
    private TaskStatus state;
    
    private Double etaRelative = 0.0;
    
    private Boolean queued = false;
    
    /**
     * 队列位置
     */
    private Integer queuePosition;
    
    private Boolean completed = false;
}
