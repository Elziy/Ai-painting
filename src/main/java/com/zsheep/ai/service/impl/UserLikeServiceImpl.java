package com.zsheep.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsheep.ai.common.exception.service.ServiceException;
import com.zsheep.ai.domain.entity.Txt2ImgTask;
import com.zsheep.ai.domain.entity.UserLike;
import com.zsheep.ai.mapper.UserLikeMapper;
import com.zsheep.ai.service.Txt2ImgTaskService;
import com.zsheep.ai.service.UserLikeService;
import com.zsheep.ai.utils.DateUtils;
import com.zsheep.ai.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 针对表【user_like】的数据库操作Service实现
 *
 * @author Elziy
 */
@Service
public class UserLikeServiceImpl extends ServiceImpl<UserLikeMapper, UserLike>
        implements UserLikeService {
    
    @Resource
    private Txt2ImgTaskService txt2ImgTaskService;
    
    @Override
    @Transactional
    public void likeTxt2ImgTask(String tid) {
        long count = txt2ImgTaskService.count(new LambdaQueryWrapper<Txt2ImgTask>().eq(Txt2ImgTask::getTid, tid));
        if (count == 0) {
            throw new ServiceException("work.is.not.found", new String[]{tid});
        }
        Long uid = SecurityUtils.getUserId();
        LambdaQueryWrapper<UserLike> wrapper = new LambdaQueryWrapper<UserLike>()
                .eq(UserLike::getTid, tid)
                .eq(UserLike::getUid, uid);
        Long likeCount = baseMapper.selectCount(wrapper);
        if (likeCount == 0) {
            UserLike userLike = new UserLike();
            userLike.setTid(tid);
            userLike.setUid(uid);
            userLike.setCreateTime(DateUtils.now());
            boolean save = this.save(userLike);
            if (!save) {
                throw new ServiceException("like.failed");
            }
        }
    }
    
    @Override
    public void dislikeTxt2ImgTask(String tid) {
        Long uid = SecurityUtils.getUserId();
        LambdaQueryWrapper<UserLike> wrapper = new LambdaQueryWrapper<UserLike>()
                .eq(UserLike::getTid, tid)
                .eq(UserLike::getUid, uid);
        this.remove(wrapper);
    }
}




