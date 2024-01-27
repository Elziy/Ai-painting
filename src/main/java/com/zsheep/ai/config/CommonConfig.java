package com.zsheep.ai.config;

import com.zsheep.ai.config.api.ApiProperties;
import com.zsheep.ai.config.api.UploadProperties;
import com.zsheep.ai.config.auth.AuthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@EnableConfigurationProperties({ApiProperties.class, AuthProperties.class, UploadProperties.class})
public class CommonConfig {
}
