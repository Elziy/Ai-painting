package com.zsheep.ai.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色
 *
 * @author Elziy
 */
@TableName(value ="role")
@Data
public class Role implements Serializable {
    @TableId
    private Long id;

    private String name;

    private String roleKey;

    private String status;

    private Integer delFlag;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    private String remark;

    private static final long serialVersionUID = 1L;
}