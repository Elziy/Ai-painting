package com.zsheep.ai.service.impl;

import com.alibaba.fastjson2.JSON;
import com.zsheep.ai.common.constants.ApiConstant;
import com.zsheep.ai.common.constants.HttpMethods;
import com.zsheep.ai.common.constants.HttpStatus;
import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.common.core.domain.model.ImageEntity;
import com.zsheep.ai.common.exception.service.ServiceException;
import com.zsheep.ai.config.api.UploadProperties;
import com.zsheep.ai.service.UploadApiService;
import com.zsheep.ai.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UploadApiServiceImpl implements UploadApiService {
    
    @Resource
    private UploadProperties uploadProperties;
    
    @Override
    public R<?> uploadImage(ImageEntity imageEntity) {
        HttpResponse response;
        try {
            Map<String, String> querys = new HashMap<>();
            String bodys = JSON.toJSONString(imageEntity);
            response = HttpUtil.doPost(uploadProperties.getUploadHost(), ApiConstant.UPLOAD, HttpMethods.POST, getHeaders(), querys, bodys);
        } catch (Exception e) {
            throw new ServiceException("调用上传图像api异常");
        }
        if (Objects.nonNull(response) && response.getStatusLine().getStatusCode() == HttpStatus.SUCCESS) {
            try {
                String json = EntityUtils.toString(response.getEntity());
                return JSON.parseObject(json, R.class);
            } catch (IOException e) {
                throw new ServiceException("解析上传图像返回结果异常");
            }
        } else {
            try {
                log.error("调用上传图像api异常{}", EntityUtils.toString(response.getEntity()));
            } catch (IOException ignored) {
            }
            throw new ServiceException("上传图像异常");
        }
    }
    
    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }
}
