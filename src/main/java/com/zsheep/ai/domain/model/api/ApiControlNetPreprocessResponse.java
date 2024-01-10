package com.zsheep.ai.domain.model.api;

import com.alibaba.fastjson2.PropertyNamingStrategy;
import com.alibaba.fastjson2.annotation.JSONType;
import lombok.Data;

import java.util.List;

@Data
@JSONType(naming = PropertyNamingStrategy.SnakeCase)
public class ApiControlNetPreprocessResponse {
    private String image;
    
    private Pose pose;
    
    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    static class People {
        private List<Double> poseKeypoints2d;
        private List<Double> faceKeypoints2d;
        private List<Double> handLeftKeypoints2d;
        private List<Double> handRightKeypoints2d;
    }
    
    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    static class Pose {
        private List<People> people;
        private List<List<Double>> animals;
        private int canvasHeight;
        private int canvasWidth;
    }
}
