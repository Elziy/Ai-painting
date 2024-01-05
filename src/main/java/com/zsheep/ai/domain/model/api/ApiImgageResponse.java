package com.zsheep.ai.domain.model.api;

import com.zsheep.ai.domain.entity.Txt2ImgParams;
import lombok.Data;

import java.util.List;

@Data
public class ApiImgageResponse {
    private List<String> images;
    
    private Txt2ImgParams parameters;
    
    private String info;
}