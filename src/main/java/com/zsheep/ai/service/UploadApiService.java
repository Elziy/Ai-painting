package com.zsheep.ai.service;

import com.zsheep.ai.common.core.domain.R;
import com.zsheep.ai.common.core.domain.model.ImageEntity;

public interface UploadApiService {
    R<?> uploadImage(ImageEntity imageEntity);
}
