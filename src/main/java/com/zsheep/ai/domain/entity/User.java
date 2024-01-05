package com.zsheep.ai.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @TableName user
 */
@TableName(value = "user")
@Data
public class User implements Serializable {
    
    @TableId
    private Long id;
    
    private String username;
    
    private String sex;
    
    private String email;
    
    private String phone;
    
    private String password;
    
    private String avatar;
    
    @TableField(exist = false)
    private List<Role> roles;
    
    private Integer status;
    
    @TableLogic
    private Integer delFlag;
    
    private String loginIp;
    
    private Date loginTime;
    
    private Date createTime;
    
    private static final long serialVersionUID = 1L;
}