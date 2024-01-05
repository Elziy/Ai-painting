package com.zsheep.ai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zsheep.ai.domain.entity.Role;
import com.zsheep.ai.service.RoleService;
import com.zsheep.ai.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
 * 针对表【role(角色表)】的数据库操作Service实现
 *
 * @author Elziy
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}




