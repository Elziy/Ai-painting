package com.zsheep.ai.domain.model;

import lombok.Data;

@Data
public class TaskProgressVo {
    private String taskId;
    
    private Boolean active = false;
    
    private Double progress = 0.0;
    
    private Double eta = 0.0;
    
    private int jobCount;
    
    /**
     * 完成的任务数
     */
    private int jobNo;
    
    /**
     * 是否在队列中
     */
    private Boolean queued = false;
    
    /**
     * 队列位置
     */
    private Integer queuePosition;
    
    private Boolean completed = false;
    
    private Txt2ImgTaskVo txt2ImgTask;
    
    private Boolean failed = false;
    
    private String failureCause;
}
