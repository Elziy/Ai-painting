package com.zsheep.ai.common.core.service;

import com.zsheep.ai.common.exception.service.ServiceException;
import com.zsheep.ai.domain.entity.Txt2ImgImage;
import com.zsheep.ai.domain.entity.Txt2ImgParams;
import com.zsheep.ai.domain.entity.Txt2ImgTask;
import com.zsheep.ai.domain.model.Txt2ImgParamsVo;
import com.zsheep.ai.service.Txt2ImgImageService;
import com.zsheep.ai.service.Txt2ImgParamsService;
import com.zsheep.ai.service.Txt2ImgTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 多线程事务管理
 *
 * @author Elziy
 */
@Slf4j
@Service
public class TxService {
    
    @Resource
    private Txt2ImgImageService txt2ImgImageService;
    
    @Resource
    private Txt2ImgParamsService txt2ImgParamsService;
    
    @Resource
    private Txt2ImgTaskService txt2ImgTaskService;
    
    @Resource
    private DataSourceTransactionManager txManager;
    
    public boolean saveTxt2Img(Txt2ImgParams params, List<Txt2ImgImage> images, Txt2ImgTask txt2ImgTask) {
        
        AtomicBoolean OK = new AtomicBoolean(Boolean.TRUE);
        //子线程等待主线程通知
        CountDownLatch mainMonitor = new CountDownLatch(1);
        // 3个子线程
        int threadCount = 3;
        CountDownLatch childMonitor = new CountDownLatch(threadCount);
        
        // 事务定义
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        
        //子线程运行结果
        List<Boolean> childResponse = Collections.synchronizedList(new ArrayList<>());
        
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        // 保存图片到数据库
        CompletableFuture<Void> imagesFuture = CompletableFuture.runAsync(() -> {
            TransactionStatus status = null;
            try {
                status = txManager.getTransaction(def);
                boolean insertBatch = txt2ImgImageService.insertBatch(images);
                if (!insertBatch) {
                    throw new ServiceException("保存图片失败");
                }
                childResponse.add(Boolean.TRUE);
                childMonitor.countDown();
                mainMonitor.await();
                if (OK.get()) {
                    txManager.commit(status);
                } else {
                    txManager.rollback(status);
                }
            } catch (Exception e) {
                childResponse.add(Boolean.FALSE);
                childMonitor.countDown();
                if (status != null) {
                    log.error("Txt2Img保存图片失败,图片:{},原因:{}", images, e.getMessage());
                    txManager.rollback(status);
                }
            }
        }, executor);
        
        // 保存参数
        CompletableFuture<Void> paramsFuture = CompletableFuture.runAsync(() -> {
            TransactionStatus status = null;
            try {
                status = txManager.getTransaction(def);
                boolean save = txt2ImgParamsService.save(params);
                if (!save) {
                    throw new ServiceException("保存参数失败");
                }
                childResponse.add(Boolean.TRUE);
                childMonitor.countDown();
                mainMonitor.await();
                if (OK.get()) {
                    txManager.commit(status);
                } else {
                    txManager.rollback(status);
                }
            } catch (Exception e) {
                childResponse.add(Boolean.FALSE);
                childMonitor.countDown();
                if (status != null) {
                    log.error("保存参数失败,参数:{},原因:{}", params, e.getMessage());
                    txManager.rollback(status);
                }
            }
        }, executor);
        
        // 保存任务
        CompletableFuture<Void> taskFuture = CompletableFuture.runAsync(() -> {
            TransactionStatus status = null;
            try {
                status = txManager.getTransaction(def);
                boolean save = txt2ImgTaskService.save(txt2ImgTask);
                if (!save) {
                    throw new ServiceException("保存任务失败");
                }
                childResponse.add(Boolean.TRUE);
                childMonitor.countDown();
                mainMonitor.await();
                if (OK.get()) {
                    txManager.commit(status);
                } else {
                    txManager.rollback(status);
                }
            } catch (Exception e) {
                childResponse.add(Boolean.FALSE);
                childMonitor.countDown();
                if (status != null) {
                    log.error("保存任务失败,参数:{},原因:{}", txt2ImgTask, e.getMessage());
                    txManager.rollback(status);
                }
            }
        }, executor);
        
        try {
            childMonitor.await();
            for (Boolean resp : childResponse) {
                if (!resp) {
                    OK.set(Boolean.FALSE);
                    break;
                }
            }
            //主线程获取结果成功，让子线程开始根据主线程的结果执行（提交或回滚）
            mainMonitor.countDown();
            CompletableFuture.allOf(paramsFuture, taskFuture, imagesFuture).join();
        } catch (Exception e) {
            log.error("主线程等待子线程执行结果异常", e);
        } finally {
            executor.shutdown();
        }
        return OK.get();
    }
    
    private void copyParamVo2Param(Txt2ImgParamsVo source, Txt2ImgParams target, String pid) {
        target.setPid(pid);
        target.setEnableHr(source.getEnableHr());
        target.setDenoisingStrength(source.getDenoisingStrength());
        target.setFirstphaseWidth(null);
        target.setFirstphaseHeight(null);
        target.setHrScale(source.getHrScale());
        target.setHrUpscaler(source.getHrUpscaler());
        target.setHrSecondPassSteps(source.getHrSecondPassSteps());
        target.setHrResizeX(null);
        target.setHrResizeY(null);
        target.setPrompt(source.getPrompt());
        target.setSeed(source.getSeed());
        target.setSubseed(source.getSubseed());
        target.setSubseedStrength(source.getSubseedStrength());
        target.setSeedResizeFromH(source.getSeedResizeFromH());
        target.setSeedResizeFromW(source.getSeedResizeFromW());
        target.setSamplerName(null);
        target.setBatchSize(null);
        target.setNIter(source.getNIter());
        target.setSteps(source.getSteps());
        target.setCfgScale(source.getCfgScale());
        target.setWidth(source.getWidth());
        target.setHeight(source.getHeight());
        target.setRestoreFaces(source.getRestoreFaces());
        target.setTiling(source.getTiling());
        target.setNegativePrompt(source.getNegativePrompt());
        // target.setEta(source.getEta());
        // target.setSChurn(source.getSChurn());
        // target.setSTmax(source.getSTmax());
        // target.setSTmin(source.getSTmin());
        // target.setSNoise(source.getSNoise());
        // target.setOverrideSettingsRestoreAfterwards(source.isOverrideSettingsRestoreAfterwards());
        target.setSamplerIndex(source.getSamplerIndex());
        // target.setScriptName(source.getScriptName());
        target.setDimensionId(source.getDimension().getId());
    }
}
