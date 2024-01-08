package com.zsheep.ai.service;

import com.zsheep.ai.common.exception.task.TaskCancelException;
import com.zsheep.ai.common.exception.task.TaskException;
import com.zsheep.ai.domain.model.*;

public interface AiPaintingService {
    /**
     * 取消任务
     *
     * @param taskId 任务ID
     * @return {@link Boolean}
     * @throws TaskException 任务异常
     */
    Boolean cancelTask(String taskId);
    
    /**
     * 文字转图片(不等待结果)
     *
     * @param taskId 任务ID
     * @param params 参数
     */
    String txt2imgAsync(Txt2ImgParamsVo params);
    
    /**
     * 获取任务的进度
     *
     * @param taskId 任务ID
     * @return {@link TaskProgressVo}
     * @throws TaskException 任务异常
     */
    TaskProgressVo getTaskProgress(String taskId);
    
    /**
     * 获取登录用户任务队列或者正在执行的任务ID
     *
     * @return {@link String} 任务ID或null
     */
    String getTaskIdByUserId();
    
    /**
     * 获取用户ID对应用户的任务队列或者正在执行的任务ID
     *
     * @param userId 用户ID
     * @return {@link String} 任务ID或null
     */
    String getTaskIdByUserId(Long userId);
    
    /**
     * 获取标签和分级
     *
     * @param params 参数
     * @return {@link TagsVo}
     */
    TagsVo getTags(TaggerParamsVo params);
}
