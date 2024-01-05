package com.zsheep.ai.common.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dimension {
    
    private static final Dimension _512x512 = new Dimension(0, 512D, 512D, "512 × 512");
    private static final Dimension _512x768 = new Dimension(1, 512D, 768D, "512 × 768");
    public static final Dimension _768x512 = new Dimension(2, 768D, 512D, "768 × 512");
    private static final Dimension _1024x1024 = new Dimension(3, 1024D, 1024D, "1024 × 1024");
    
    private static final Map<Integer, Dimension> DIMENSION_MAP = new HashMap<>();
    
    static {
        DIMENSION_MAP.put(_512x512.getId(), _512x512);
        DIMENSION_MAP.put(_512x768.getId(), _512x768);
        DIMENSION_MAP.put(_768x512.getId(), _768x512);
        DIMENSION_MAP.put(_1024x1024.getId(), _1024x1024);
    }
    
    private int id;
    private double width;
    private double height;
    private String name;
    
    public static Dimension getDimension(int dimensionId) {
        return DIMENSION_MAP.get(dimensionId);
    }
}
