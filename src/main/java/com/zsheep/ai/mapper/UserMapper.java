package com.zsheep.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zsheep.ai.domain.entity.User;
import com.zsheep.ai.domain.model.UserWorkParamsVo;
import com.zsheep.ai.domain.model.UserInfoVo;
import org.apache.ibatis.annotations.Param;

/**
 * 针对表【user(用户)】的数据库操作Mapper
 *
 * @author Elziy
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据邮箱查询用户及角色信息
     *
     * @param email 用户邮箱
     * @return {@link User}
     */
    User selectUserByEmail(@Param("email") String email);
    
    UserInfoVo getUserInfoVoByUid(@Param("params") UserWorkParamsVo params, @Param("uid") Long uid, @Param("self") boolean self);
}




