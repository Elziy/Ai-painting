package com.zsheep.ai.common.core.service;

import com.zsheep.ai.common.constants.HttpStatus;
import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.common.core.domain.model.ImageEntity;
import com.zsheep.ai.config.api.ApiProperties;
import com.zsheep.ai.config.api.UploadProperties;
import com.zsheep.ai.domain.entity.Txt2ImgImage;
import com.zsheep.ai.domain.model.Txt2ImgImageVo;
import com.zsheep.ai.service.UploadApiService;
import com.zsheep.ai.utils.Base64Util;
import com.zsheep.ai.utils.DateUtils;
import com.zsheep.ai.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
public class FileService {
    
    @Resource
    private ApiProperties apiProperties;
    
    @Resource
    private UploadApiService uploadApiService;
    
    @Resource
    private UploadProperties uploadProperties;
    
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;
    
    public final SimpleDateFormat sdf;
    
    public FileService(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
        sdf = new SimpleDateFormat(apiProperties.getFileNameDateFormat());
    }
    
    public List<String> saveImages(List<Txt2ImgImageVo> images) {
        List<String> result = new ArrayList<>();
        Date date = DateUtils.now();
        String format = sdf.format(date);
        for (Txt2ImgImageVo image : images) {
            CompletableFuture<String> imageUrl = getImageUrlFuture(image, format);
            result.add(imageUrl.join());
        }
        return result;
    }
    
    public List<Txt2ImgImage> saveImages(List<Txt2ImgImageVo> images, String taskId) {
        List<Txt2ImgImage> result = new ArrayList<>();
        Date date = DateUtils.now();
        String format = sdf.format(date);
        for (Txt2ImgImageVo image : images) {
            Txt2ImgImage txt2ImgImage = getTxt2ImgImage(taskId, image);
            CompletableFuture<String> imageUrl = getImageUrlFuture(image, format);
            txt2ImgImage.setImageUrl(imageUrl.join());
            result.add(txt2ImgImage);
        }
        return result;
    }
    
    private CompletableFuture<String> getImageUrlFuture(Txt2ImgImageVo image, String format) {
        boolean notSaveToImagesServer = StringUtils.isEmpty(uploadProperties.getUploadHost())
                || StringUtils.isEmpty(uploadProperties.getImageHost());
        String filePath = apiProperties.getFilePath() + "/images/";
        if (notSaveToImagesServer) {
            log.info("图片保存到本地");
            return CompletableFuture.supplyAsync(() -> {
                String fileName = format + image.getImageId() + "." + apiProperties.getFileType();
                String imgFilePath = filePath + fileName;
                try {
                    Base64Util.generateImage(image.getImageUrl(), imgFilePath);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
                return "/images/" + fileName;
            }, threadPoolExecutor);
        } else {
            log.info("图片保存到图片服务器");
            return CompletableFuture.supplyAsync(() -> {
                String fileName = format + image.getImageId() + "." + apiProperties.getFileType();
                String imgFilePath = filePath + fileName;
                try {
                    ImageEntity imageEntity = new ImageEntity(image.getImageUrl(), imgFilePath);
                    R<?> r = uploadApiService.uploadImage(imageEntity);
                    int code = r.getCode();
                    if (code != HttpStatus.SUCCESS) {
                        throw new RuntimeException(r.getMessage());
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
                return "/images/" + fileName;
            }, threadPoolExecutor);
        }
    }
    
    private static Txt2ImgImage getTxt2ImgImage(String taskId, Txt2ImgImageVo image) {
        Txt2ImgImage txt2ImgImage = new Txt2ImgImage();
        txt2ImgImage.setImageId(image.getImageId());
        txt2ImgImage.setWidth(image.getWidth());
        txt2ImgImage.setHeight(image.getHeight());
        txt2ImgImage.setIsPublic(false);
        txt2ImgImage.setSeed(image.getSeed());
        txt2ImgImage.setCreateTime(image.getCreateTime());
        txt2ImgImage.setTid(taskId);
        return txt2ImgImage;
    }
}
