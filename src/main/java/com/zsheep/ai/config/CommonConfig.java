package com.zsheep.ai.config;

import com.zsheep.ai.config.api.ApiProperties;
import com.zsheep.ai.config.auth.AuthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ApiProperties.class, AuthProperties.class})
public class CommonConfig {
}
