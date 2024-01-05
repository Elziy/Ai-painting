package com.zsheep.ai.utils.uuid;

import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.RandomUtil;

/**
 * 随机工具类
 *
 * @author Elziy
 */
public class RandomUtils extends RandomUtil {
    
    public static final String ALLCHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    
    /**
     * 随机字符串
     *
     * @param length 长度
     * @return {@link String}
     */
    public static String randomString(int length) {
        return RandomUtil.randomString(ALLCHAR, length);
    }
    
    /**
     * 随机生成器
     *
     * @param length 长度
     * @return {@link RandomGenerator}
     */
    public static RandomGenerator randomGenerator(int length) {
        return new RandomGenerator(ALLCHAR, length);
    }
}
