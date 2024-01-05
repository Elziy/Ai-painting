package com.zsheep.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsheep.ai.common.core.page.PageInfo;
import com.zsheep.ai.domain.entity.User;
import com.zsheep.ai.domain.model.UserWorkParamsVo;
import com.zsheep.ai.domain.model.UserInfoVo;
import com.zsheep.ai.domain.model.UserWorkVo;
import com.zsheep.ai.mapper.UserMapper;
import com.zsheep.ai.service.Txt2ImgTaskService;
import com.zsheep.ai.service.UserService;
import com.zsheep.ai.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 针对表【user(用户)】的数据库操作Service实现
 *
 * @author Elziy
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    
    @Resource
    private Txt2ImgTaskService txt2ImgTaskService;
    
    @Override
    public boolean checkEmailUnique(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        Long count = this.baseMapper.selectCount(queryWrapper);
        return count == 0;
    }
    
    @Override
    public User selectUserByEmail(String email) {
        return this.baseMapper.selectUserByEmail(email);
    }
    
    @Override
    public UserInfoVo getUserInfoVoByUid(UserWorkParamsVo params, Long uid) {
        return this.baseMapper.getUserInfoVoByUid(params, uid, uid.equals(SecurityUtils.tryGetUid()));
    }
    
    @Override
    public PageInfo getUserWorks(Page<UserWorkVo> page, UserWorkParamsVo params, Long uid) {
        return txt2ImgTaskService.getUserWorks(page, params, uid);
    }
    
    @Override
    public PageInfo getUserLikes(Page<UserWorkVo> page, UserWorkParamsVo params, Long uid) {
        return txt2ImgTaskService.getUserLikes(page, params, uid);
    }
}




