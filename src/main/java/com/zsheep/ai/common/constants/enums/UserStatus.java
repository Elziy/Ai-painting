package com.zsheep.ai.common.constants.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum UserStatus {
    NORMAL(0, "正常"),
    DISABLE(1, "禁用");
    
    @EnumValue
    private Integer code;
    private String desc;
    
    UserStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public Integer getCode() {
        return code;
    }
    
    public void setCode(Integer code) {
        this.code = code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        this.desc = desc;
    }
}
