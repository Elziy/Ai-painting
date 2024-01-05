package com.zsheep.ai.domain.entity;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName stable_diffusion_model
 */
@Data
@TableName(value = "stable_diffusion_model")
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
public class StableDiffusionModel implements Serializable {
    @TableId
    private String hash;
    
    private String title;
    
    private String description;
    
    private String modelName;
    
    private String filename;
    
    private String modelImage;
    
    private String sha256;
    
    private Integer status;
    
    private Long delflag;
    
    private Date createTime;
    
    private static final long serialVersionUID = 1L;
}