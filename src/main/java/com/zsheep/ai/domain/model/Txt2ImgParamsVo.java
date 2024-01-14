package com.zsheep.ai.domain.model;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import com.zsheep.ai.common.core.domain.model.Dimension;
import com.zsheep.ai.common.core.valid.DecimalMultiple;
import com.zsheep.ai.domain.model.api.OverrideSettings;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
public class Txt2ImgParamsVo {
    
    private String pid;
    
    /**
     * 提示
     */
    @NotBlank(message = "{prompt.not.blank}")
    private String prompt;
    
    /**
     * 否定提示
     */
    private String negativePrompt;
    
    /**
     * 取样器算法
     */
    private String samplerIndex = "Euler a";
    
    /**
     * 生成批次
     */
    private Integer nIter = 1;
    
    /**
     * 步数
     */
    private Integer steps = 28;
    
    /**
     * 提示词相关性
     */
    private double cfgScale = 11;
    
    private Integer dimensionId = 1;
    
    /**
     * 尺寸
     */
    @NotNull(message = "{dimension.not.null}")
    private Dimension dimension;
    
    @Min(value = 64, message = "{width.min}")
    @Max(value = 1024, message = "{width.max}")
    @DecimalMultiple(value = 1, message = "{width.multiple}")
    private double width = 512;
    
    @Min(value = 64, message = "{height.min}")
    @Max(value = 1024, message = "{height.max}")
    @DecimalMultiple(value = 1, message = "{height.multiple}")
    private double height = 512;
    
    /**
     * 面部修复
     */
    private Boolean restoreFaces;
    
    /**
     * 可平铺
     */
    private Boolean tiling;
    
    //region Hires.fix
    private Boolean enableHr;
    
    /**
     * 去噪强度(重绘幅度)
     */
    @DecimalMin(value = "0.01", message = "{denoisingStrength.min}")
    @DecimalMax(value = "1", message = "{denoisingStrength.max}")
    @DecimalMultiple(value = 0.01, message = "{denoisingStrength.multiple}")
    private double denoisingStrength = 0.01;
    
    /**
     * 第二次放大采样步数
     */
    private Integer hrSecondPassSteps = 10;
    
    /**
     * 放大倍数
     */
    @DecimalMin(value = "1", message = "{hrScale.min}")
    @DecimalMax(value = "2", message = "{hrScale.max}")
    private Double hrScale = 1.2;
    /**
     * 放大算法
     */
    private String hrUpscaler = "SwinIR_4x";
    //endregion
    
    // private List<String> styles;
    private Long seed = -1L;
    private Long subseed = -1L;
    private Long subseedStrength;
    private Long seedResizeFromH;
    private Long seedResizeFromW;
    
    // private Integer eta;
    // private Integer sChurn;
    // private Integer sTmax;
    // private Integer sTmin;
    // private Integer sNoise = 1;
    private OverrideSettings overrideSettings;
    private Boolean overrideSettingsRestoreAfterwards = true;
    // private List<String> script_args;
    //
    // private String scriptName;
    
    private Map<String, Object> alwaysonScripts;
    
    {
        alwaysonScripts = new HashMap<>(1);
        alwaysonScripts.put("LoRA Block Weight", new Object[]{"NONE:0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\nALL:1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1\nINS:1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0\nIND:1,0,0,0,1,1,1,0,0,0,0,0,0,0,0,0,0\nINALL:1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0\nPALETTE:1,0,0,0,0,0,0,0,0,0,0,0.8,1,1,1,1,1\n\nMIDD:1,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0\nOUTD:1,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0\nOUTS:1,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1\nOUTALL:1,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1\nALL0.5:0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5,0.5",
                true,
                "Disable",
                "values",
                "0,0.25,0.5,0.75,1",
                "Block ID",
                "IN05-OUT05",
                "none",
                "",
                "0.5,1",
                "BASE,IN00,IN01,IN02,IN03,IN04,IN05,IN06,IN07,IN08,IN09,IN10,IN11,M00,OUT00,OUT01,OUT02,OUT03,OUT04,OUT05,OUT06,OUT07,OUT08,OUT09,OUT10,OUT11",
                1,
                "black",
                "20",
                false,
                "ATTNDEEPON:IN05-OUT05:attn:1\n\nATTNDEEPOFF:IN05-OUT05:attn:0\n\nPROJDEEPOFF:IN05-OUT05:proj:0\n\nXYZ:::1",
                false});
    }
    
    private String model;
    
    private ControlNet controlNet;
    
    @SuppressWarnings("unused")
    public void setDimensionId(int dimensionId) {
        this.dimensionId = dimensionId;
        Dimension d = Dimension.getDimension(dimensionId);
        if (d != null) {
            this.dimension = d;
        } else {
            this.dimension = new Dimension(dimensionId, width, height, width + " × " + height);
        }
    }
    
    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class ControlNet {
        List<ControlNetArgs> args;
    }
    
    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class ControlNetArgs {
        private Boolean enabled = true;
        private String module;
        private String moduleOriginal;
        private String model;
        private Double weight = 1.0;
        private String image;
        private String inputImage;
        private Integer resizeMode;
        private Boolean lowvram = false;
        private Integer processorRes = 512;
        private Integer thresholdA = -1;
        private Integer thresholdB = -1;
        private Double guidanceStart = 0.0;
        private Double guidanceEnd = 1.0;
        private Integer controlMode = 0;
        private Boolean pixelPerfect = false;
        
        @SuppressWarnings("unused")
        public void setModule(String module) {
            this.moduleOriginal = module;
            this.module = "none";
        }
    }
}
