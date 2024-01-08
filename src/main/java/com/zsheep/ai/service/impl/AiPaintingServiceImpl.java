package com.zsheep.ai.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zsheep.ai.common.constants.CommonConstant;
import com.zsheep.ai.common.constants.enums.DecimalPlaces;
import com.zsheep.ai.common.constants.enums.TaggerModel;
import com.zsheep.ai.common.core.domain.model.TaskMap;
import com.zsheep.ai.common.core.service.FileService;
import com.zsheep.ai.common.core.task.TaskExecutor;
import com.zsheep.ai.common.exception.service.ServiceException;
import com.zsheep.ai.common.exception.task.TaskCancelException;
import com.zsheep.ai.common.exception.task.TaskException;
import com.zsheep.ai.config.api.ApiProperties;
import com.zsheep.ai.config.thread.MyFutureTask;
import com.zsheep.ai.domain.entity.Txt2ImgImage;
import com.zsheep.ai.domain.entity.Txt2ImgParams;
import com.zsheep.ai.domain.entity.Txt2ImgTask;
import com.zsheep.ai.domain.model.*;
import com.zsheep.ai.domain.model.api.ApiImgageResponse;
import com.zsheep.ai.domain.model.api.ApiTaggerResponse;
import com.zsheep.ai.domain.model.api.ApiTaskProgress;
import com.zsheep.ai.domain.model.api.OverrideSettings;
import com.zsheep.ai.service.AIApiService;
import com.zsheep.ai.service.AiPaintingService;
import com.zsheep.ai.service.StableDiffusionModelService;
import com.zsheep.ai.service.Txt2ImgTaskService;
import com.zsheep.ai.utils.DateUtils;
import com.zsheep.ai.utils.MessageUtils;
import com.zsheep.ai.utils.SecurityUtils;
import com.zsheep.ai.utils.uuid.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class AiPaintingServiceImpl implements AiPaintingService {
    
    @Resource
    private AIApiService aiApiService;
    
    @Resource
    private TaskExecutor taskExecutor;
    
    @Resource(name = "finishTaskMap")
    TaskMap<String, TaskProgressVo> finishTaskMap;
    
    @Resource
    private FileService fileService;
    
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    
    @Resource
    private Txt2ImgTaskService txt2ImgTaskService;
    
    @Resource
    private StableDiffusionModelService stableDiffusionModelService;
    
    /**
     * 图片base64编码格式前缀
     */
    public final String BASE64_PREFIX;
    
    public AiPaintingServiceImpl(ApiProperties properties) {
        this.BASE64_PREFIX = "data:image/" + properties.getFileType() + ";base64,";
    }
    
    @Override
    public Boolean cancelTask(String taskId) {
        MyFutureTask<Void> currentTask = taskExecutor.getCurrentTask();
        if (Objects.nonNull(currentTask) && currentTask.getTaskId().equals(taskId)) {
            throw new TaskException("task.in.progress", null);
        }
        Long userId = SecurityUtils.getUserId();
        return taskExecutor.cancelTask(taskId, userId);
    }
    
    @Override
    public Txt2ImgTaskVo txt2img(String taskId, Txt2ImgParamsVo params) {
        Long uid = SecurityUtils.getUserId();
        checkTaskId(taskId, uid);
        initParams(params);
        Future<?> submit = taskExecutor.submit(() -> aiApiService.getImageByApi(params), taskId, uid);
        ApiImgageResponse apiImgageResponse;
        try {
            Object o = submit.get();
            apiImgageResponse = (ApiImgageResponse) o;
            apiImgageResponse.getParameters().setPid(taskId);
            apiImgageResponse.getParameters().setDimensionId(params.getDimension().getId());
            apiImgageResponse.getParameters().setInfo(apiImgageResponse.getInfo());
        } catch (CancellationException e) {
            log.error("任务{}被取消", taskId);
            taskFailed(taskId, MessageUtils.message("task.is.cancel"));
            throw new TaskCancelException();
        } catch (InterruptedException e) {
            log.error("任务{}被中断", taskId);
            taskFailed(taskId, MessageUtils.message("task.is.interrupt"));
            throw new TaskException("task.is.interrupt", null);
        } catch (Exception e) {
            log.error("获取任务{}执行结果异常,原因:{}", taskId, e.getMessage());
            taskFailed(taskId, MessageUtils.message("task.generate.image.is.error"));
            throw new TaskCancelException();
        }
        
        List<String> imageBase64List = apiImgageResponse.getImages();
        if (Objects.isNull(imageBase64List) || imageBase64List.isEmpty()) {
            throw new TaskException("task.generate.image.is.error", null);
        }
        List<Txt2ImgImageVo> txt2ImgImageVoBase64List = getTxt2ImgImageVos(params, imageBase64List);
        Txt2ImgTaskVo txt2ImgTaskVo = getTxt2ImgTaskVo(taskId, params, uid, txt2ImgImageVoBase64List);
        taskSuccess(taskId, txt2ImgTaskVo);
        // 保存完成任务信息
        Txt2ImgParams parameters = apiImgageResponse.getParameters();
        saveTask(taskId, uid, txt2ImgImageVoBase64List, parameters);
        return txt2ImgTaskVo;
    }
    
    @Override
    public String txt2imgAsync(Txt2ImgParamsVo params) {
        Long uid = SecurityUtils.getUserId();
        
        String taskId = initTaskId(uid);
        initParams(params);
        
        Future<?> submit = taskExecutor.submit(() -> {
            ApiImgageResponse apiImgageResponse = aiApiService.getImageByApi(params);
            Txt2ImgParams parameters = apiImgageResponse.getParameters();
            parameters.setPid(taskId);
            parameters.setDimensionId(params.getDimension().getId());
            parameters.setInfo(apiImgageResponse.getInfo());
            parameters.setSdModelCheckpoint(parameters.getOverrideSettings().getSdModelCheckpoint());
            parameters.setClipStopAtLastLayers(parameters.getOverrideSettings().getClipStopAtLastLayers());
            parameters.setEtaNoiseSeedDelta(parameters.getOverrideSettings().getEtaNoiseSeedDelta());
            
            List<String> imageBase64List = apiImgageResponse.getImages();
            if (Objects.isNull(imageBase64List) || imageBase64List.isEmpty()) {
                throw new TaskException("task.generate.image.is.error", null);
            }
            List<Txt2ImgImageVo> txt2ImgImageVoBase64List = getTxt2ImgImageVos(params, imageBase64List);
            Txt2ImgTaskVo txt2ImgTaskVo = getTxt2ImgTaskVo(taskId, params, uid, txt2ImgImageVoBase64List);
            // 完成任务
            taskSuccess(taskId, txt2ImgTaskVo);
            // 保存任务信息
            saveTask(taskId, uid, txt2ImgImageVoBase64List, parameters);
        }, taskId, uid);
        
        new Thread(() -> {
            try {
                submit.get();
            } catch (CancellationException e) {
                log.error("任务{}被取消", taskId);
            } catch (InterruptedException e) {
                log.error("任务{}被中断", taskId);
                TaskProgressVo taskProgressVo = new TaskProgressVo();
                taskProgressVo.setTaskId(taskId);
                taskProgressVo.setFailed(true);
                taskProgressVo.setFailureCause(MessageUtils.message("task.is.interrupt"));
                finishTaskMap.put(taskId, taskProgressVo);
            } catch (Exception e) {
                log.error("获取任务{}执行结果异常,原因:{}", taskId, e.getMessage());
                TaskProgressVo taskProgressVo = new TaskProgressVo();
                taskProgressVo.setTaskId(taskId);
                taskProgressVo.setFailed(true);
                taskProgressVo.setFailureCause(MessageUtils.message("task.generate.image.is.error"));
                finishTaskMap.put(taskId, taskProgressVo);
            }
        }).start();
        
        return taskId;
    }
    
    /**
     * 获取任务的进度
     *
     * @param taskId 任务ID
     * @return {@link TaskProgressVo}
     * @throws TaskException 任务异常
     */
    @Override
    public TaskProgressVo getTaskProgress(String taskId) {
        if (Objects.isNull(taskId)) {
            throw new TaskException("task.id.not.null", null);
        }
        
        TaskProgressVo taskProgressVo;
        
        MyFutureTask<Void> currentTask = taskExecutor.getCurrentTask();
        if (Objects.nonNull(currentTask) && currentTask.getTaskId().equals(taskId)) {
            ApiTaskProgress apiTaskProgress;
            try {
                apiTaskProgress = aiApiService.getTaskProgressByApi();
                taskProgressVo = new TaskProgressVo();
                taskProgressVo.setTaskId(taskId);
                taskProgressVo.setProgress(apiTaskProgress.getProgress());
                taskProgressVo.setEta(apiTaskProgress.getEtaRelative());
                taskProgressVo.setActive(true);
                String job = apiTaskProgress.getState().getJob();
                // if (StrUtil.isBlank(job)) {
                //     // 1张
                //     if (apiTaskProgress.getState().getJobCount() == 1) {
                //         taskProgressVo.setJobCount(apiTaskProgress.getState().getJobCount());
                //         taskProgressVo.setJobNo(apiTaskProgress.getState().getJobNo());
                //     } else {
                //         taskProgressVo.setJobCount(apiTaskProgress.getState().getJobCount() / 2);
                //         taskProgressVo.setJobNo(apiTaskProgress.getState().getJobNo() / 2);
                //     }
                // } else {
                //     try {
                //         // Batch 1 out of 2
                //         String[] split = job.split(" ");
                //         System.out.println("split = " + Arrays.toString(split));
                //         taskProgressVo.setJobCount(Integer.parseInt(split[4]));
                //         taskProgressVo.setJobNo(Integer.parseInt(split[1]) - 1);
                //     } catch (NumberFormatException e) {
                //         taskProgressVo.setJobCount(apiTaskProgress.getState().getJobCount());
                //         taskProgressVo.setJobNo(apiTaskProgress.getState().getJobNo());
                //     }
                // }
                taskProgressVo.setJobCount(apiTaskProgress.getState().getJobCount());
                taskProgressVo.setJobNo(apiTaskProgress.getState().getJobNo());
                return taskProgressVo;
            } catch (Exception e) {
                throw new TaskException("task.generate.image.is.error", null);
            }
        }
        
        int taskIndex = taskExecutor.getTaskQueueIndex(taskId);
        if (taskIndex != -1) {
            taskProgressVo = new TaskProgressVo();
            taskProgressVo.setTaskId(taskId);
            taskProgressVo.setQueued(true);
            taskProgressVo.setQueuePosition(taskIndex + 1);
            return taskProgressVo;
        }
        
        TaskProgressVo finish = finishTaskMap.get(taskId);
        if (Objects.nonNull(finish)) {
            Txt2ImgTaskVo txt2ImgTask = finish.getTxt2ImgTask();
            if (Objects.nonNull(txt2ImgTask) && !SecurityUtils.getUserId().equals(txt2ImgTask.getUid()))
                throw new TaskException("task.is.not.yours", new String[]{taskId});
            return finish;
        }
        
        throw new TaskException("task.not.found", new Object[]{taskId});
    }
    
    @Override
    public String getTaskIdByUserId() {
        return getTaskIdByUserId(SecurityUtils.getUserId());
    }
    
    @Override
    public String getTaskIdByUserId(Long userId) {
        return taskExecutor.getTaskIdByUserId(userId);
    }
    
    @Override
    public TagsVo getTags(TaggerParamsVo params) {
        String model = params.getModel();
        TaggerModel taggerModel = TaggerModel.getByName(model);
        if (Objects.isNull(taggerModel)) {
            throw new ServiceException("tagger.model.not.support", new String[]{model});
        }
        params.setModel(taggerModel.getModel());
        ApiTaggerResponse apiTaggerResponse = aiApiService.getTagsByApi(params);
        TagsVo tagsVo = new TagsVo();
        LinkedHashMap<String, BigDecimal> tags = apiTaggerResponse.getTags();
        // 将tags的name拼成一个字符串
        if (CollectionUtils.isNotEmpty(tags)) {
            tagsVo.setTags(String.join(", ", tags.keySet()));
        }
        tagsVo.setGeneralTags(apiTaggerResponse.getGeneralTags());
        tagsVo.setCharacterTags(apiTaggerResponse.getCharacterTags());
        ApiTaggerResponse.Ratings ratings = apiTaggerResponse.getRatings();
        if (Objects.nonNull(ratings)) {
            tagsVo.setRating(ratings.getRating());
        }
        return tagsVo;
    }
    
    /**
     * 检查任务ID
     *
     * @param taskId 任务ID
     * @param uid    用户ID
     */
    private void checkTaskId(String taskId, Long uid) {
        if (Objects.isNull(taskId)) {
            throw new TaskException("task.id.not.null", null);
        }
        
        boolean finish = txt2ImgTaskService.isTaskIdFinish(taskId);
        if (finish) {
            throw new TaskException("task.is.already.finished", null);
        }
        
        // 同一用户只能有一个任务, 且任务ID不能重复
        taskExecutor.isTaskExist(taskId, uid);
        
        MyFutureTask<Void> currentTask = taskExecutor.getCurrentTask();
        if (Objects.nonNull(currentTask) && currentTask.getTaskId().equals(taskId)) {
            throw new TaskException("task.in.progress", null);
        }
    }
    
    private String initTaskId(Long uid) {
        do {
            String taskId = RandomUtils.randomString(CommonConstant.TASK_ID_LENGTH);
            if (!txt2ImgTaskService.isTaskIdFinish(taskId)) {
                return taskId;
            }
        } while (true);
    }
    
    /**
     * 初始化参数
     *
     * @param params 参数个数
     */
    private void initParams(Txt2ImgParamsVo params) {
        if (params.getSeed() == -1) {
            params.setSeed(Long.valueOf(RandomUtil.randomNumbers(CommonConstant.SEED_LENGTH)));
        }
        
        if (Objects.nonNull(params.getModel())) {
            OverrideSettings overrideSettings = params.getOverrideSettings();
            if (Objects.isNull(overrideSettings)) {
                overrideSettings = new OverrideSettings();
                params.setOverrideSettings(overrideSettings);
            }
            overrideSettings.setSdModelCheckpoint(null);
            List<StableDiffusionModelVo> stableDiffusionModels = stableDiffusionModelService.getAllModelsFromRedis();
            for (StableDiffusionModelVo model : stableDiffusionModels) {
                if (model.getHash().equals(params.getModel())) {
                    overrideSettings.setSdModelCheckpoint(model.getHash());
                    break;
                }
            }
        }
    }
    
    /**
     * 获取任务信息
     *
     * @param taskId                   任务ID
     * @param params                   参数个数
     * @param uid                      用户ID
     * @param txt2ImgImageVoBase64List txt2img图像信息base64列表
     * @return {@link Txt2ImgTaskVo}
     */
    private Txt2ImgTaskVo getTxt2ImgTaskVo(String taskId, Txt2ImgParamsVo params, Long uid, List<Txt2ImgImageVo> txt2ImgImageVoBase64List) {
        Txt2ImgTaskVo txt2ImgTaskVo = new Txt2ImgTaskVo();
        txt2ImgTaskVo.setTid(taskId);
        txt2ImgTaskVo.setUid(uid);
        txt2ImgTaskVo.setParameters(params);
        txt2ImgTaskVo.setImages(txt2ImgImageVoBase64List);
        txt2ImgTaskVo.setCreateTime(DateUtils.now());
        return txt2ImgTaskVo;
    }
    
    /**
     * 将任务放入完成任务队列
     *
     * @param taskId        任务ID
     * @param txt2ImgTaskVo 任务对象
     */
    private void taskSuccess(String taskId, Txt2ImgTaskVo txt2ImgTaskVo) {
        TaskProgressVo taskProgressVo = new TaskProgressVo();
        taskProgressVo.setTaskId(taskId);
        taskProgressVo.setProgress(100.0D);
        taskProgressVo.setCompleted(true);
        taskProgressVo.setTxt2ImgTask(txt2ImgTaskVo);
        finishTaskMap.put(taskId, taskProgressVo);
    }
    
    private void taskFailed(String taskId, String failureCause) {
        TaskProgressVo taskProgressVo = new TaskProgressVo();
        taskProgressVo.setTaskId(taskId);
        taskProgressVo.setFailed(true);
        taskProgressVo.setFailureCause(failureCause);
        finishTaskMap.put(taskId, taskProgressVo);
    }
    
    /**
     * 将图片base64字符转换为图片对象
     *
     * @param params          参数
     * @param imageBase64List 图片base64字符列表
     * @return {@link List}<{@link Txt2ImgImageVo}>
     */
    private List<Txt2ImgImageVo> getTxt2ImgImageVos(Txt2ImgParamsVo params, List<String> imageBase64List) {
        List<Txt2ImgImageVo> txt2ImgImageVoBase64List = new ArrayList<>(imageBase64List.size());
        Long seed = params.getSeed();
        AtomicInteger i = new AtomicInteger(0);
        for (String imageBase64 : imageBase64List) {
            Txt2ImgImageVo img = new Txt2ImgImageVo();
            img.setImageId(RandomUtils.randomString(CommonConstant.IMAGE_ID_LENGTH));
            img.setImageUrl(BASE64_PREFIX + imageBase64);
            if (params.getEnableHr()) {
                double h = params.getHeight() * params.getHrScale();
                double w = params.getWidth() * params.getHrScale();
                BigDecimal height = new BigDecimal(h).setScale(DecimalPlaces.ZERO.value(), RoundingMode.HALF_UP);
                BigDecimal width = new BigDecimal(w).setScale(DecimalPlaces.ZERO.value(), RoundingMode.HALF_UP);
                img.setHeight(height.doubleValue());
                img.setWidth(width.doubleValue());
            } else {
                img.setHeight(params.getHeight());
                img.setWidth(params.getWidth());
            }
            img.setCreateTime(new Date());
            img.setSeed(seed + i.getAndIncrement());
            txt2ImgImageVoBase64List.add(img);
        }
        return txt2ImgImageVoBase64List;
    }
    
    /**
     * 保存任务信息
     *
     * @param taskId                   任务ID
     * @param uid                      用户ID
     * @param txt2ImgImageVoBase64List txt2img图像信息base64列表
     * @param parameters               参数
     */
    private void saveTask(String taskId, Long uid, List<Txt2ImgImageVo> txt2ImgImageVoBase64List, Txt2ImgParams parameters) {
        // 保存图片文件/OSS
        CompletableFuture<List<Txt2ImgImage>> saveImageFiles = CompletableFuture.supplyAsync(() ->
                fileService.saveImagesToImagesServer(txt2ImgImageVoBase64List, taskId), threadPoolExecutor);
        
        saveImageFiles.whenComplete((txt2ImgImages, throwable) -> {
            if (Objects.nonNull(throwable)) {
                log.error("保存图片文件异常,原因:{}", throwable.getMessage());
                return;
            }
            Txt2ImgTask txt2ImgTask = new Txt2ImgTask();
            txt2ImgTask.setTid(taskId);
            txt2ImgTask.setUid(uid);
            txt2ImgTask.setPid(taskId);
            Date now = DateUtils.now();
            txt2ImgTask.setCreateTime(now);
            parameters.setCreateTime(now);
            CompletableFuture.runAsync(() ->
                    txt2ImgTaskService.saveTxt2ImgTask(parameters, txt2ImgImages, txt2ImgTask), threadPoolExecutor);
        });
    }
}
