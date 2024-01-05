package com.zsheep.ai.domain.model;

import com.zsheep.ai.common.core.valid.Multiple;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class TestValidation {
    @NotBlank(message = "提示词不能为空")
    private String prompt;
    
    @Min(value = 1, message = "最小值为1")
    @Multiple(value = 2, message = "种子{validate.multiple}")
    private Integer seed;
}
