package com.zsheep.ai.config;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import com.zsheep.ai.config.i18n.AcceptHeaderLocaleResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Value("${spring.mvc.format.date-time}")
    private String pattern = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 配置消息转换器
     *
     * @param converters 消息转换器列表
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //需要定义一个convert转换消息的对象;
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //添加fastJson的配置信息;
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setDateFormat(pattern);
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);
        
        fastJsonConfig.setWriterFeatures(
                // 序列化输出空值字段
                JSONWriter.Feature.WriteNulls,
                // 在大范围超过JavaScript支持的整数，输出为字符串格式
                JSONWriter.Feature.BrowserCompatible,
                //  List字段如果为null,输出为[],而非null
                JSONWriter.Feature.WriteNullListAsEmpty,
                // 将Boolean类型字段的空值序列化输出为false
                JSONWriter.Feature.WriteNullBooleanAsFalse,
                JSONWriter.Feature.WriteLongAsString
        );
        
        fastJsonConfig.setReaderFeatures(
                JSONReader.Feature.SupportSmartMatch
        );
        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(0, fastConverter);
    }
    
    /**
     * 配置日期格式化
     *
     * @param registry 格式化注册器
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter(pattern));
    }
    
    /**
     * 配置国际化
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new AcceptHeaderLocaleResolver();
    }
}
