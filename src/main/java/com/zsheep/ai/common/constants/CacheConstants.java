package com.zsheep.ai.common.constants;

import java.util.concurrent.TimeUnit;

/**
 * 缓存常量
 *
 * @author Elziy
 */
public class CacheConstants {
    
    public static final Object NULL_VALUE = null;
    
    /**
     * 分隔符
     */
    public static final String SEPARATOR = ":";
    
    /**
     * null值过期时间
     */
    public static final int NULL_VALUE_EXPIRE_TIME = 5;
    
    /**
     * null值过期时间单位
     */
    public static final TimeUnit NULL_VALUE_EXPIRE_TIME_UNIT = TimeUnit.MINUTES;
    
    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";
    
    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_count:";
    
    /**
     * 文生图作品缓存
     */
    public static final String TXT_2_IMG_WORK_KEY = "txt2img_work:";
    
    /**
     * 图片的tid缓存
     */
    public static final String IMAGE_TID = "image_tid:";
    
    public static final String STABLE_DIFFUSION_MODELS = "stable_diffusion_models";
    
    public static final String ControlNetTypes = "control_net_types";
}
