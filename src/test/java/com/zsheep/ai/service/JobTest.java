package com.zsheep.ai.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.lang.Console;
import com.zsheep.ai.utils.DateUtils;
import com.zsheep.ai.utils.uuid.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// @SpringBootTest
public class JobTest {
    public static void main(String[] args) {
        String job = "Batch 1 out of 2";
        String[] split = job.split(" ");
        System.out.println(Integer.parseInt(split[4]));
        System.out.println(Integer.parseInt(split[1]));
    }
    
    @Test
    public void test() {
        BigDecimal s = BigDecimal.valueOf(0.5);
        BigDecimal v = BigDecimal.valueOf(0.01);
        boolean b = s.remainder(v).compareTo(BigDecimal.ZERO) == 0;
        System.out.println(b);
    }
    
    @Test
    public void testTime() {
        Date now = DateUtils.now();
        List<Date> dates = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dates.add(DateUtils.addMinutes(now, i));
        }
        System.out.println(dates);
        dates.sort((a, b) -> {
            return Long.compare(b.getTime(), a.getTime());
        });
        System.out.println(dates);
    }
    
    @Resource
    private DataSourceTransactionManager txManager;
    
    @Resource
    private Txt2ImgTaskService service;
    
    @Test
    public void test2() {
        RandomGenerator generator = RandomUtils.randomGenerator(6);
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100, 6, 20);
        lineCaptcha.setGenerator(generator);
        lineCaptcha.createCode();
        String imageBase64 = lineCaptcha.getImageBase64();
        System.out.println(imageBase64);
        //输出code
        Console.log(lineCaptcha.getCode());
    }
}
