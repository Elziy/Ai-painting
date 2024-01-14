package com.zsheep.ai.service.impl;

import com.alibaba.fastjson2.JSON;
import com.zsheep.ai.common.constants.ApiConstant;
import com.zsheep.ai.common.constants.HttpMethods;
import com.zsheep.ai.common.constants.HttpStatus;
import com.zsheep.ai.common.exception.service.ServiceException;
import com.zsheep.ai.config.api.ApiProperties;
import com.zsheep.ai.domain.entity.StableDiffusionModel;
import com.zsheep.ai.domain.model.ControlNetPreprocessParamsVo;
import com.zsheep.ai.domain.model.TaggerParamsVo;
import com.zsheep.ai.domain.model.Txt2ImgParamsVo;
import com.zsheep.ai.domain.model.api.ApiControlNetPreprocessResponse;
import com.zsheep.ai.domain.model.api.ApiImgageResponse;
import com.zsheep.ai.domain.model.api.ApiTaggerResponse;
import com.zsheep.ai.domain.model.api.ApiTaskProgress;
import com.zsheep.ai.service.AIApiService;
import com.zsheep.ai.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class AIApiServiceImpl implements AIApiService {
    
    @Resource
    private ApiProperties apiProperties;
    
    @Override
    public ApiImgageResponse getImageByApi(Txt2ImgParamsVo params) {
        HttpResponse response;
        try {
            Map<String, String> querys = new HashMap<>();
            String bodys = JSON.toJSONString(params);
            response = HttpUtil.doPost(apiProperties.getHost(), ApiConstant.TXT_2_IMG, HttpMethods.POST, getHeaders(), querys, bodys);
        } catch (Exception e) {
            throw new ServiceException("调用txt2img绘图api异常");
        }
        if (Objects.nonNull(response) && response.getStatusLine().getStatusCode() == HttpStatus.SUCCESS) {
            try {
                String json = EntityUtils.toString(response.getEntity());
                return JSON.parseObject(json, ApiImgageResponse.class);
            } catch (IOException e) {
                throw new ServiceException("解析txt2img返回结果异常");
            }
        } else {
            try {
                log.error("调用txt2img绘图api异常{}", EntityUtils.toString(response.getEntity()));
            } catch (IOException ignored) {
            }
            throw new ServiceException("参数异常");
        }
    }
    
    @Override
    public ApiTaskProgress getTaskProgressByApi() {
        HttpResponse response;
        try {
            Map<String, String> querys = new HashMap<>();
            response = HttpUtil.doGet(apiProperties.getHost(), ApiConstant.GET_TASK_PROGRESS, HttpMethods.GET, getHeaders(), querys);
        } catch (Exception e) {
            throw new ServiceException("调用进度查询api异常");
        }
        
        if (Objects.nonNull(response) && response.getStatusLine().getStatusCode() == HttpStatus.SUCCESS) {
            try {
                String json = EntityUtils.toString(response.getEntity());
                return JSON.parseObject(json, ApiTaskProgress.class);
            } catch (IOException e) {
                throw new ServiceException("解析进度查询api返回结果异常");
            }
        } else {
            try {
                log.error("调用进度查询api异常{}", EntityUtils.toString(response.getEntity()));
            } catch (IOException ignored) {
            }
            throw new ServiceException("接口异常");
        }
    }
    
    @Override
    public ApiTaggerResponse getTagsByApi(TaggerParamsVo params) {
        HttpResponse response;
        try {
            Map<String, String> querys = new HashMap<>();
            String bodys = JSON.toJSONString(params);
            response = HttpUtil.doPost(apiProperties.getHost(), ApiConstant.GET_TAGS, HttpMethods.POST, getHeaders(), querys, bodys);
        } catch (Exception e) {
            throw new ServiceException("调用查询标签api异常");
        }
        if (Objects.nonNull(response) && response.getStatusLine().getStatusCode() == HttpStatus.SUCCESS) {
            try {
                String json = EntityUtils.toString(response.getEntity());
                return JSON.parseObject(json, ApiTaggerResponse.class);
            } catch (IOException e) {
                throw new ServiceException("解析查询标签api返回结果异常");
            }
        } else {
            try {
                log.error("调用查询标签api异常{}", EntityUtils.toString(response.getEntity()));
            } catch (IOException ignored) {
            }
            throw new ServiceException("参数异常");
        }
    }
    
    @Override
    public List<StableDiffusionModel> getStableDiffusionModelsByApi() {
        HttpResponse response;
        try {
            response = HttpUtil.doGet(apiProperties.getHost(), ApiConstant.GET_STABLE_DIFFUSION_MODELS, HttpMethods.GET, getHeaders(), null);
        } catch (Exception e) {
            throw new ServiceException("调用查询模型api异常");
        }
        if (Objects.nonNull(response) && response.getStatusLine().getStatusCode() == HttpStatus.SUCCESS) {
            try {
                String json = EntityUtils.toString(response.getEntity());
                return JSON.parseArray(json, StableDiffusionModel.class);
            } catch (IOException e) {
                throw new ServiceException("解析查询模型api返回结果异常");
            }
        } else {
            try {
                log.error("调用查询模型api异常{}", EntityUtils.toString(response.getEntity()));
            } catch (IOException ignored) {
            }
            throw new ServiceException("调用查询模型api异常");
        }
    }
    
    @Override
    public ApiControlNetPreprocessResponse getControlNetPreprocessByApi(ControlNetPreprocessParamsVo params) {
        HttpResponse response;
        try {
            Map<String, String> querys = new HashMap<>();
            String bodys = JSON.toJSONString(params);
            response = HttpUtil.doPost(apiProperties.getHost(), ApiConstant.CONTROLNET_PREPROCESS, HttpMethods.POST, getHeaders(), querys, bodys);
        } catch (Exception e) {
            throw new ServiceException("调用控制网络预处理api异常");
        }
        if (Objects.nonNull(response) && response.getStatusLine().getStatusCode() == HttpStatus.SUCCESS) {
            try {
                String json = EntityUtils.toString(response.getEntity());
                return JSON.parseObject(json, ApiControlNetPreprocessResponse.class);
            } catch (IOException e) {
                throw new ServiceException("解析控制网络预处理api返回结果异常");
            }
        } else {
            try {
                log.error("调用控制网络预处理api异常{}", EntityUtils.toString(response.getEntity()));
            } catch (IOException ignored) {
            }
            throw new ServiceException("参数异常");
        }
    }
    
    @Override
    public String getControlNetTypeByApi() {
        HttpResponse response;
        try {
            response = HttpUtil.doGet(apiProperties.getHost(), ApiConstant.CONTROLNET_TYPE, HttpMethods.GET, getHeaders(), null);
        } catch (Exception e) {
            throw new ServiceException("调用控制网络类型api异常");
        }
        if (Objects.nonNull(response) && response.getStatusLine().getStatusCode() == HttpStatus.SUCCESS) {
            try {
                return EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                throw new ServiceException("解析控制网络类型api返回结果异常");
            }
        } else {
            try {
                log.error("调用控制网络类型api异常{}", EntityUtils.toString(response.getEntity()));
            } catch (IOException ignored) {
            }
            throw new ServiceException("调用控制网络类型api异常");
        }
    }
    
    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return headers;
    }
}
