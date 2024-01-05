package com.zsheep.ai.common.exception.task;

/**
 * 任务取消异常
 *
 * @author Elziy
 */
public class TaskCancelException extends TaskException {
    public TaskCancelException() {
        super("task.cancel.success", null);
    }
}
