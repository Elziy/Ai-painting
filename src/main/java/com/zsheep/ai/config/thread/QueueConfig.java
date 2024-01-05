package com.zsheep.ai.config.thread;

import com.zsheep.ai.common.constants.CommonConstant;
import com.zsheep.ai.config.api.ApiProperties;
import com.zsheep.ai.common.core.domain.model.TaskMap;
import com.zsheep.ai.domain.model.TaskProgressVo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class QueueConfig {
    
    @Bean(name = "taskQueue")
    public LinkedBlockingQueue<Runnable> getQueue() {
        return new LinkedBlockingQueue<>(CommonConstant.MAX_TASK_NUM);
    }
    
    @Bean(name = "finishTaskMap")
    public TaskMap<String, TaskProgressVo> getFinishTaskMap(ApiProperties apiProperties) {
        return new TaskMap<>(apiProperties.getFinishTaskMapSize());
    }
}
