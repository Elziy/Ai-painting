package com.zsheep.ai.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zsheep.ai.common.core.page.PageInfo;
import com.zsheep.ai.domain.entity.User;
import com.zsheep.ai.domain.model.UserWorkParamsVo;
import com.zsheep.ai.domain.model.UserInfoVo;
import com.zsheep.ai.domain.model.UserWorkVo;

/**
 * 针对表【user(用户)】的数据库操作Service
 *
 * @author Elziy
 */
public interface UserService extends IService<User> {
    
    /**
     * 检查用户名是否唯一
     *
     * @param username 用户名
     * @return {@link String}
     */
    boolean checkEmailUnique(String email);
    
    /**
     * 根据邮箱查询用户及角色信息
     *
     * @param email 用户名
     * @return {@link User}
     */
    User selectUserByEmail(String email);
    
    UserInfoVo getUserInfoVoByUid(UserWorkParamsVo params, Long uid);
    
    PageInfo getUserWorks(Page<UserWorkVo> page, UserWorkParamsVo params, Long uid);
    
    PageInfo getUserLikes(Page<UserWorkVo> page, UserWorkParamsVo params, Long uid);
}
