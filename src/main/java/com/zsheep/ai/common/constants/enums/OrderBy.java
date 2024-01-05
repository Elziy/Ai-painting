package com.zsheep.ai.common.constants.enums;

public enum OrderBy {
    
    // 随机
    Random("rand()", "Random"),
    CreateTimeDesc("create_time desc", "CreateTimeDesc"),
    CreateTimeAsc("create_time asc", "CreateTimeAsc");
    
    private final String value;
    
    private final String key;
    
    OrderBy(String value, String key) {
        this.value = value;
        this.key = key;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getKey() {
        return key;
    }
    
    public static OrderBy getEnumByKey(String key) {
        if (null == key) {
            return null;
        }
        for (OrderBy temp : OrderBy.values()) {
            if (temp.getKey().equals(key)) {
                return temp;
            }
        }
        return null;
    }
}
