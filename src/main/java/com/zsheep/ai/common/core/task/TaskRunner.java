package com.zsheep.ai.common.core.task;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TaskRunner implements CommandLineRunner {
    
    private final TaskExecutor taskExecutor;
    
    public TaskRunner(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // 启动任务执行器
        taskExecutor.start();
    }
}

