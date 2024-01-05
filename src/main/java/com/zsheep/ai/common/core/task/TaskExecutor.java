package com.zsheep.ai.common.core.task;

import com.zsheep.ai.common.exception.task.TaskException;
import com.zsheep.ai.config.thread.MyFutureTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Component
@Slf4j
public class TaskExecutor {
    
    @Resource(name = "singleThreadPoolExecutor")
    private ExecutorService executor;
    
    @Resource
    private BlockingQueue<Runnable> taskQueue;
    
    private volatile MyFutureTask<Void> currentTask;
    
    
    @SuppressWarnings({"InfiniteLoopStatement", "unchecked"})
    public void start() {
        executor.execute(() -> {
            while (true) {
                try {
                    Runnable task = taskQueue.take();
                    if (task instanceof MyFutureTask) {
                        currentTask = (MyFutureTask<Void>) task;
                    } else {
                        log.error("任务队列异常");
                    }
                    task.run();
                    currentTask = null;
                } catch (Exception e) {
                    log.error("任务执行异常{}", e.getMessage());
                }
            }
        });
    }
    
    /**
     * 提交有返回值的任务
     *
     * @param task   任务
     * @param taskId 任务ID
     * @return {@link Future}
     */
    public Future<?> submit(Callable<Object> task, String taskId, Long userId) {
        MyFutureTask<Object> myFutureTask = new MyFutureTask<>(task, taskId, userId);
        taskQueue.add(myFutureTask);
        return myFutureTask;
    }
    
    /**
     * 提交无返回值的任务
     *
     * @param task 任务
     * @return {@link Future}
     */
    public Future<Void> submit(Runnable task, String taskId, Long userId) {
        MyFutureTask<Void> myFutureTask = new MyFutureTask<>(task, taskId, userId);
        taskQueue.add(myFutureTask);
        return myFutureTask;
    }
    
    /**
     * 获取任务队列大小
     *
     * @return int
     */
    public int getTaskQueueSize() {
        return taskQueue.size();
    }
    
    /**
     * 获取任务队列索引
     *
     * @param taskId id
     * @return int 任务的索引, -1表示不存在
     */
    @SuppressWarnings("unchecked")
    public int getTaskQueueIndex(String taskId) {
        int index = 0;
        for (Runnable runnable : taskQueue) {
            if (runnable instanceof MyFutureTask) {
                MyFutureTask<Object> myFutureTask = (MyFutureTask<Object>) runnable;
                if (myFutureTask.getTaskId().equals(taskId)) {
                    return index;
                }
            }
            index++;
        }
        return -1;
    }
    
    /**
     * 获取当前任务
     *
     * @return MyFutureTask
     */
    public MyFutureTask<Void> getCurrentTask() {
        return currentTask;
    }
    
    /**
     * 取消任务
     *
     * @param taskId id
     * @return boolean
     */
    @SuppressWarnings({"unchecked"})
    public boolean cancelTask(String taskId, Long userId) {
        if (taskQueue.isEmpty()) {
            return false;
        }
        for (Runnable runnable : taskQueue) {
            if (runnable instanceof MyFutureTask) {
                MyFutureTask<Object> myFutureTask = (MyFutureTask<Object>) runnable;
                if (myFutureTask.getTaskId().equals(taskId) && myFutureTask.getUserId().equals(userId)) {
                    myFutureTask.cancel(true);
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * 任务队列中是否存在taskId的任务或userId用户的任务
     *
     * @param taskId 任务ID
     * @param userId 用户ID
     * @throws TaskException 任务异常, 任务已存在或用户已存在任务
     */
    @SuppressWarnings("unchecked")
    public void isTaskExist(String taskId, Long userId) {
        if (taskQueue.isEmpty()) {
            return;
        }
        for (Runnable runnable : taskQueue) {
            if (runnable instanceof MyFutureTask) {
                MyFutureTask<Object> myFutureTask = (MyFutureTask<Object>) runnable;
                if (myFutureTask.getTaskId().equals(taskId)) {
                    throw new TaskException("task.is.already.exist", new String[]{taskId});
                } else if (myFutureTask.getUserId().equals(userId)) {
                    throw new TaskException("task.user.is.already.exist", new String[]{taskId});
                }
            }
        }
    }
    
    /**
     * 获取任务ID通过用户ID
     *
     * @param userId 用户ID
     * @return {@link String} 任务ID或null
     */
    @SuppressWarnings("unchecked")
    public String getTaskIdByUserId(Long userId) {
        if (currentTask != null && currentTask.getUserId().equals(userId)) {
            return currentTask.getTaskId();
        }
        if (taskQueue.isEmpty()) {
            return null;
        }
        for (Runnable runnable : taskQueue) {
            if (runnable instanceof MyFutureTask) {
                MyFutureTask<Object> myFutureTask = (MyFutureTask<Object>) runnable;
                if (myFutureTask.getUserId().equals(userId)) {
                    return myFutureTask.getTaskId();
                }
            }
        }
        return null;
    }
}

