package com.zsheep.ai.domain.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserWorkParamsVo {
    private Long limit;
    
    private String orderBy;
    
    private Date startTime;
}
