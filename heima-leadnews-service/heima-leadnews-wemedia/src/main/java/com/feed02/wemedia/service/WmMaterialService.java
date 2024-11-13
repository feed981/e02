package com.feed02.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feed02.model.common.dtos.ResponseResult;
import com.feed02.model.wemedia.pojos.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

public interface WmMaterialService extends IService<WmMaterial> {
    public ResponseResult uploadPicture(MultipartFile multipartFile);
}
