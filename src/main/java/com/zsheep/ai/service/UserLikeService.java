package com.zsheep.ai.service;

import com.zsheep.ai.domain.entity.UserLike;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 针对表【user_like】的数据库操作Service
 *
 * @author Elziy
 */
public interface UserLikeService extends IService<UserLike> {
    
    void likeTxt2ImgTask(String tid);
    
    void dislikeTxt2ImgTask(String tid);
}
