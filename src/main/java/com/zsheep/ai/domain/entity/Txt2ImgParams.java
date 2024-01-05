package com.zsheep.ai.domain.entity;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zsheep.ai.common.core.domain.model.Dimension;
import com.zsheep.ai.domain.model.api.OverrideSettings;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 文本转图片参数表
 *
 * @author Elziy
 */
@Data
@TableName(value ="txt_2_img_params")
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
public class Txt2ImgParams implements Serializable {
    @TableId
    private String pid;

    private Boolean enableHr;

    private Double denoisingStrength;

    private Double firstphaseWidth;

    private Double firstphaseHeight;

    private Double hrScale;

    private String hrUpscaler;

    private Integer hrSecondPassSteps;

    private Double hrResizeX;

    private Double hrResizeY;

    private String prompt;

    private Long seed;

    private Long subseed;

    private Long subseedStrength;

    private Long seedResizeFromH;

    private Long seedResizeFromW;

    private String samplerName;

    private Integer batchSize;

    private Integer nIter;

    private Integer steps;

    private Double cfgScale;

    private Double width;

    private Double height;

    private Boolean restoreFaces;

    private Boolean tiling;

    private String negativePrompt;

    private Integer eta;

    private Integer sChurn;

    private Integer sTmax;

    private Integer sTmin;

    private Integer sNoise;
    
    @TableField(exist = false)
    private OverrideSettings overrideSettings;

    private Boolean overrideSettingsRestoreAfterwards;

    private String samplerIndex;

    private String scriptName;

    private Integer dimensionId;
    
    @TableField(exist = false)
    private Dimension dimension;
    
    /**
     * sd模型检查点
     */
    private String sdModelCheckpoint;
    
    /**
     * eta噪音种子δ
     */
    private Long etaNoiseSeedDelta;
    
    /**
     * Clip skip
     */
    private Long clipStopAtLastLayers;
    
    /**
     * 创作信息
     */
    private String info;
    
    private Date createTime;
    
    @TableLogic
    private Integer delFlag;

    private static final long serialVersionUID = 1L;
}