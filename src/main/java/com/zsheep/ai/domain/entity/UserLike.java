package com.zsheep.ai.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName user_like
 */
@TableName(value ="user_like")
@Data
public class UserLike implements Serializable {
    @TableId
    private Long uid;
    
    private String tid;

    private Date createTime;

    private static final long serialVersionUID = 1L;
}