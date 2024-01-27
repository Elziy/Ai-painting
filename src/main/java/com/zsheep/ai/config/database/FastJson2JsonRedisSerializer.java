package com.zsheep.ai.config.database;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Redis使用FastJson序列化
 *
 * @author Elziy
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    
    private final Class<T> clazz;
    
    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }
    
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONString(t, JSONWriter.Feature.WriteClassName,
                JSONWriter.Feature.WriteNulls,
                // 在大范围超过JavaScript支持的整数，输出为字符串格式
                JSONWriter.Feature.BrowserCompatible,
                //  List字段如果为null,输出为[],而非null
                JSONWriter.Feature.WriteNullListAsEmpty,
                // 将Boolean类型字段的空值序列化输出为false
                JSONWriter.Feature.WriteNullBooleanAsFalse,
                JSONWriter.Feature.WriteLongAsString).getBytes(DEFAULT_CHARSET);
    }
    
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);
        
        return JSON.parseObject(str, clazz, JSONReader.Feature.SupportAutoType);
    }
}
