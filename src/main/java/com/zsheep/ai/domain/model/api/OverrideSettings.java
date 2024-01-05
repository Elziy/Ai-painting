package com.zsheep.ai.domain.model.api;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

@Data
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
public class OverrideSettings {
    
    /**
     * sd模型检查点 Model hash: a757fe8b3d
     */
    private String sdModelCheckpoint;
    
    /**
     * eta噪音种子δ
     */
    private long etaNoiseSeedDelta = 31337L;
    
    /**
     * Clip skip
     */
    @JSONField(name = "CLIP_stop_at_last_layers")
    private Long clipStopAtLastLayers = 2L;
    
    @JSONField(name = "enable_pnginfo")
    private final static Boolean enablePngInfo = false;
}
