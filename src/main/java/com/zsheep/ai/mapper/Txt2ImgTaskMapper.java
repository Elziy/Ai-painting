package com.zsheep.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zsheep.ai.domain.entity.Txt2ImgTask;
import com.zsheep.ai.domain.model.Txt2ImgTaskVo;
import com.zsheep.ai.domain.model.UserWorkParamsVo;
import com.zsheep.ai.domain.model.UserWorkVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 针对表【txt_2_img_task】的数据库操作Mapper
 *
 * @author Elziy
 */
public interface Txt2ImgTaskMapper extends BaseMapper<Txt2ImgTask> {
    
    /**
     * 根据用户ID查询自己的所有或非自己公开的任务
     *
     * @param uid    用户ID
     * @param params 参数个数
     * @return {@link List}<{@link Txt2ImgTaskVo}>
     */
    List<Txt2ImgTaskVo> selectTxt2ImgTaskVoByUid(@Param("uid") Long uid, @Param("params") UserWorkParamsVo params, Boolean self);
    
    /**
     * 查询推荐图片
     *
     * @param params 参数
     * @return {@link List}<{@link UserWorkVo}>
     */
    List<UserWorkVo> selectRecommendImages(@Param("params") UserWorkParamsVo params, @Param("uid") Long uid);
    
    /**
     * 根据任务ID查询推荐图片
     *
     * @param tid tid
     * @param uid 用户ID
     * @return {@link UserWorkVo}
     */
    UserWorkVo selectUserWorkByTid(@Param("tid") String tid, @Param("uid") Long uid);
    
    /**
     * 查询用户的作品
     *
     * @param page   页面
     * @param params 参数个数
     * @param uid    用户ID
     * @param loginUid 登录用户ID
     * @return {@link Page}<{@link UserWorkVo}>
     */
    Page<UserWorkVo> selectUserWorks(@Param("page") Page<UserWorkVo> page, @Param("params") UserWorkParamsVo params, @Param("uid") Long uid, @Param("loginUid") Long loginUid);
    
    /**
     * 查询用户点赞的图片
     *
     * @param page   分页
     * @param params 参数
     * @param uid    用户ID
     * @return {@link Page}<{@link UserWorkVo}>
     */
    Page<UserWorkVo> selectUserLikes(@Param("page") Page<UserWorkVo> page, @Param("params") UserWorkParamsVo params, @Param("uid") Long uid, @Param("self") Boolean self);
}




