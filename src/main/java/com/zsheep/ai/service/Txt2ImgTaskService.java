package com.zsheep.ai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsheep.ai.common.core.page.PageInfo;
import com.zsheep.ai.domain.entity.Txt2ImgImage;
import com.zsheep.ai.domain.entity.Txt2ImgParams;
import com.zsheep.ai.domain.entity.Txt2ImgTask;
import com.zsheep.ai.domain.model.Txt2ImgTaskVo;
import com.zsheep.ai.domain.model.UserWorkParamsVo;
import com.zsheep.ai.domain.model.UserWorkVo;

import java.util.List;

/**
 * 针对表【txt_2_img_task】的数据库操作Service
 *
 * @author Elziy
 */
public interface Txt2ImgTaskService extends IService<Txt2ImgTask> {
    
    boolean isTaskIdFinish(String taskId);
    
    Boolean saveTxt2ImgTask(Txt2ImgParams params, List<Txt2ImgImage> images, Txt2ImgTask txt2ImgTask);
    
    List<Txt2ImgTaskVo> getTxt2ImgTaskVoBySelf(UserWorkParamsVo params);
    
    List<Txt2ImgTaskVo> getTxt2ImgTaskVoByUid(Long uid, UserWorkParamsVo params);
    
    List<UserWorkVo> getRecommendImages(UserWorkParamsVo params);
    
    UserWorkVo getUserWorkByTid(String tid);
    
    PageInfo getUserWorks(Page<UserWorkVo> page, UserWorkParamsVo params, Long uid);
    
    PageInfo getUserLikes(Page<UserWorkVo> page, UserWorkParamsVo params, Long uid);
}
