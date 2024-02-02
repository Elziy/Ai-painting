package com.zsheep.ai.domain.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 模型对象 models
 *
 * @author elziy6
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("models")
public class Model implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 模型ID
     */
    @TableId(value = "id")
    private Long id;
    
    /**
     * 类型
     */
    private String type;
    
    /**
     * 父模型ID
     */
    private Long parentId;
    
    /**
     * Hash
     */
    private String hash;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 说明
     */
    private String description;
    
    /**
     * 版本
     */
    private String modelVersion;
    
    /**
     * 名称
     */
    private String modelName;
    
    /**
     * 图像
     */
    private String modelImage;
    
    /**
     * 文件名
     */
    private String filename;
    
    /**
     * sha256
     */
    private String sha256;
    
    /**
     * 状态
     */
    private Long status;
    
    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    
    @TableLogic
    @JSONField(serialize = false)
    private Integer delFlag;
    
    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
}
