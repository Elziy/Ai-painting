package com.zsheep.ai.domain.model.api;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

@Data
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
public class TaskStatus {
    private boolean skipped;
    
    private boolean interrupted;
    
    private String job;
    
    private int jobCount;
    
    private String jobTimestamp;
    
    /**
     * 完成的任务数
     */
    private int jobNo;
    
    private int samplingStep;
    
    private int samplingSteps;
    
}
