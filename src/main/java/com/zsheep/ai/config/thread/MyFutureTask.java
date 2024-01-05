package com.zsheep.ai.config.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class MyFutureTask<V> extends FutureTask<V> {
    
    private String taskId;
    
    private Long userId;
    
    public MyFutureTask(Callable<V> callable, String taskId, Long userId) {
        super(callable);
        this.taskId = taskId;
        this.userId = userId;
    }
    
    public MyFutureTask(Runnable runnable, V result, String taskId, Long userId) {
        super(runnable, result);
        this.taskId = taskId;
        this.userId = userId;
    }
    
    public MyFutureTask(Runnable runnable, String taskId, Long userId) {
        super(runnable, null);
        this.taskId = taskId;
        this.userId = userId;
    }
    
    public String getTaskId() {
        return taskId;
    }
    
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
