package com.zsheep.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsheep.ai.common.constants.CacheConstants;
import com.zsheep.ai.common.constants.enums.OrderBy;
import com.zsheep.ai.common.core.page.PageInfo;
import com.zsheep.ai.common.core.redis.RedisCache;
import com.zsheep.ai.common.exception.service.ServiceException;
import com.zsheep.ai.domain.entity.Txt2ImgImage;
import com.zsheep.ai.domain.entity.Txt2ImgParams;
import com.zsheep.ai.domain.entity.Txt2ImgTask;
import com.zsheep.ai.domain.model.Txt2ImgImageVo;
import com.zsheep.ai.domain.model.Txt2ImgTaskVo;
import com.zsheep.ai.domain.model.UserWorkParamsVo;
import com.zsheep.ai.domain.model.UserWorkVo;
import com.zsheep.ai.mapper.Txt2ImgTaskMapper;
import com.zsheep.ai.service.Txt2ImgImageService;
import com.zsheep.ai.service.Txt2ImgParamsService;
import com.zsheep.ai.service.Txt2ImgTaskService;
import com.zsheep.ai.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.zsheep.ai.utils.SecurityUtils.tryGetUid;

/**
 * 针对表【txt_2_img_task】的数据库操作Service实现
 *
 * @author Elziy
 */
@Slf4j
@Service
public class Txt2ImgTaskServiceImpl extends ServiceImpl<Txt2ImgTaskMapper, Txt2ImgTask> implements Txt2ImgTaskService {
    
    @Resource
    private TransactionTemplate transactionTemplate;
    
    @Resource
    private Txt2ImgImageService txt2ImgImageService;
    
    @Resource
    private Txt2ImgParamsService txt2ImgParamsService;
    
    @Resource
    private RedisCache redisCache;
    
    @Override
    public boolean isTaskIdFinish(String taskId) {
        LambdaQueryWrapper<Txt2ImgTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Txt2ImgTask::getTid, taskId);
        long count = this.count(queryWrapper);
        return count > 0;
    }
    
    @Override
    public Boolean saveTxt2ImgTask(Txt2ImgParams params, List<Txt2ImgImage> images, Txt2ImgTask txt2ImgTask) {
        AtomicBoolean OK = new AtomicBoolean(Boolean.TRUE);
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    boolean saveParams = txt2ImgParamsService.save(params);
                    if (!saveParams) {
                        log.error("Txt2Img保存参数失败,参数:{}", params);
                        throw new ServiceException("保存参数失败");
                    }
                    boolean saveImages = txt2ImgImageService.insertBatch(images);
                    if (!saveImages) {
                        log.error("Txt2Img保存图片失败,图片:{}", images);
                        throw new ServiceException("保存图片失败");
                    }
                    boolean saveTask = save(txt2ImgTask);
                    if (!saveTask) {
                        log.error("Txt2Img保存任务失败,任务:{}", txt2ImgTask);
                        throw new ServiceException("保存任务失败");
                    }
                    log.info("Txt2Img保存任务成功,任务:{}", txt2ImgTask);
                    status.flush();
                } catch (Exception e) {
                    OK.set(Boolean.FALSE);
                    status.setRollbackOnly();
                }
            }
        });
        return OK.get();
    }
    
    @Override
    public List<Txt2ImgTaskVo> getTxt2ImgTaskVoBySelf(UserWorkParamsVo params) {
        return baseMapper.selectTxt2ImgTaskVoByUid(SecurityUtils.getUserId(), params, true);
    }
    
    @Override
    public List<Txt2ImgTaskVo> getTxt2ImgTaskVoByUid(Long uid, UserWorkParamsVo params) {
        return baseMapper.selectTxt2ImgTaskVoByUid(uid, params, uid.equals(SecurityUtils.tryGetUid()));
    }
    
    @Override
    public List<UserWorkVo> getRecommendImages(UserWorkParamsVo params) {
        OrderBy orderBy = OrderBy.getEnumByKey(params.getOrderBy());
        if (orderBy != null) {
            params.setOrderBy(orderBy.getValue());
        } else {
            params.setOrderBy(OrderBy.Random.getValue());
        }
        Long uid = tryGetUid();
        List<UserWorkVo> userWorkVos = baseMapper.selectRecommendImages(params, uid);
        for (UserWorkVo userWorkVo : userWorkVos) {
            String tid = userWorkVo.getTid();
            List<Txt2ImgImageVo> txt2ImgImageVos = txt2ImgImageService.getImagesByTid(tid, uid);
            userWorkVo.setImages(txt2ImgImageVos);
        }
        return userWorkVos;
    }
    
    @Override
    public UserWorkVo getUserWorkByTid(String tid) {
        Long uid = tryGetUid();
        
        UserWorkVo workFromRedis = redisCache.getCacheObject(CacheConstants.TXT_2_IMG_WORK_KEY + tid);
        if (workFromRedis != null) {
            if (uid.equals(workFromRedis.getUid())) {
                return baseMapper.selectUserWorkByTid(tid, uid);
            }
            return workFromRedis;
        }
        UserWorkVo work = baseMapper.selectUserWorkByTid(tid, uid);
        if (work == null) {
            redisCache.setCacheObject(CacheConstants.TXT_2_IMG_WORK_KEY + tid,
                    CacheConstants.NULL_VALUE,
                    CacheConstants.NULL_VALUE_EXPIRE_TIME,
                    CacheConstants.NULL_VALUE_EXPIRE_TIME_UNIT);
            return null;
        }
        if (uid.equals(work.getUid())) {
            return work;
        }
        redisCache.setCacheObject(CacheConstants.TXT_2_IMG_WORK_KEY + tid, work);
        return work;
    }
    
    @Override
    public PageInfo getUserWorks(Page<UserWorkVo> page, UserWorkParamsVo params, Long uid) {
        Page<UserWorkVo> userWorkVoPage = baseMapper.selectUserWorks(page, params, uid, SecurityUtils.tryGetUid());
        List<UserWorkVo> userWorkVos = userWorkVoPage.getRecords();
        for (UserWorkVo userWorkVo : userWorkVos) {
            String tid = userWorkVo.getTid();
            List<Txt2ImgImageVo> txt2ImgImageVos = txt2ImgImageService.getImagesByTid(tid, uid);
            userWorkVo.setImages(txt2ImgImageVos);
        }
        return new PageInfo(userWorkVoPage.getCurrent(), userWorkVoPage.getSize(), userWorkVoPage.getTotal(), userWorkVos);
    }
    
    @Override
    public PageInfo getUserLikes(Page<UserWorkVo> page, UserWorkParamsVo params, Long uid) {
        Page<UserWorkVo> userWorkVoPage = baseMapper.selectUserLikes(page, params, uid, uid.equals(SecurityUtils.tryGetUid()));
        List<UserWorkVo> userWorkVos = userWorkVoPage.getRecords();
        for (UserWorkVo userWorkVo : userWorkVos) {
            String tid = userWorkVo.getTid();
            List<Txt2ImgImageVo> txt2ImgImageVos = txt2ImgImageService.getImagesByTid(tid, uid);
            userWorkVo.setImages(txt2ImgImageVos);
            userWorkVo.setIsLike(true);
        }
        return new PageInfo(userWorkVoPage.getCurrent(), userWorkVoPage.getSize(), userWorkVoPage.getTotal(), userWorkVos);
    }
}




