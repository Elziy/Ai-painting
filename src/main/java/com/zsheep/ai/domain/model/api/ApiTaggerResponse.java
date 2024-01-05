package com.zsheep.ai.domain.model.api;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * api标签和分级响应
 *
 * @author Elziy
 */
@Data
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
public class ApiTaggerResponse {
    private Ratings ratings;
    
    private LinkedHashMap<String, BigDecimal> tags;
    
    private LinkedHashMap<String, BigDecimal> generalTags;
    
    private LinkedHashMap<String, BigDecimal> characterTags;
    
    @Data
    public static class Ratings {
        /**
         * 大众级/普通内容
         */
        private BigDecimal general;
        
        /**
         * 轻微性暗示/敏感内容
         */
        private BigDecimal sensitive;
        
        /**
         * 强烈性暗示/可疑内容
         */
        private BigDecimal questionable;
        
        /**
         * 色情/露骨内容
         */
        private BigDecimal explicit;
        
        public String getRating() {
            BigDecimal max = general;
            String rating = general != null ? "general" : "unknown";
            if (sensitive != null && sensitive.compareTo(max) > 0) {
                max = sensitive;
                rating = "sensitive";
            }
            if (questionable != null && questionable.compareTo(max) > 0) {
                max = questionable;
                rating = "questionable";
            }
            if (explicit != null && explicit.compareTo(max) > 0) {
                rating = "explicit";
            }
            return rating;
        }
    }
}
