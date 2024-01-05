package com.zsheep.ai.common.core.domain.model;

import lombok.Data;

@Data
public class TurnstileRequest {
    String secret;
    String response;
    String remoteip;
}
