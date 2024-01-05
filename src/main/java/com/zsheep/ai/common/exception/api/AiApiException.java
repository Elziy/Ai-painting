package com.zsheep.ai.common.exception.api;

import com.zsheep.ai.common.exception.base.BaseException;

/**
 * ai api异常
 *
 * @author Elziy
 */
public class AiApiException extends BaseException {
    public AiApiException(String code, Object[] args) {
        super("task", code, args, "任务异常");
    }
}
