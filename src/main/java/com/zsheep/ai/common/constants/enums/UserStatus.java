package com.zsheep.ai.common.constants.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum UserStatus {
    NORMAL(1, "正常"),
    DISABLE(0, "禁用");
    
    @EnumValue
    private Integer code;
    private String desc;
}
