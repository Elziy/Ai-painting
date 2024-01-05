package com.zsheep.ai.common.core.domain.model;

import lombok.Data;

@Data
public class TurnstileResponse {
    Boolean success;
    String hostname;
    String action;
}
