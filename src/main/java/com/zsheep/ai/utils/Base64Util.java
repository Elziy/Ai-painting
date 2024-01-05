package com.zsheep.ai.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Base64Util {
    /**
     * 对字节数组字符串进行Base64解码并生成图片
     *
     * @param base64Str   base64 str
     * @param imgFilePath img文件路径
     */
    public static void GenerateImage(String base64Str, String imgFilePath) {
        if (base64Str == null) // 图像数据为空
            return;
        base64Str = getBase64(base64Str);
        try {
            // Base64解码
            byte[] bytes = Base64.decodeBase64(base64Str);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成图片
            OutputStream out = Files.newOutputStream(Paths.get(imgFilePath));
            out.write(bytes);
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new RuntimeException("Base64解码并生成图片异常");
        }
    }
    
    // 去除base64字符串头部的data:image/png;base64,
    public static String getBase64(String base64Str) {
        if (StringUtils.isEmpty(base64Str))
            return base64Str;
        if (base64Str.contains(",")) {
            return base64Str.substring(base64Str.indexOf(",") + 1);
        }
        return base64Str;
        
    }
}

