package com.zsheep.ai.common.exception.task;

import com.zsheep.ai.common.exception.base.BaseException;

/**
 * 任务异常
 *
 * @author Elziy
 */
public class TaskException extends BaseException {
    private static final long serialVersionUID = 1L;
    
    public TaskException(String code, Object[] args) {
        super("task", code, args, "任务异常");
    }
}
