package com.feed02.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feed02.model.common.dtos.ResponseResult;
import com.feed02.model.wemedia.dtos.WmNewsPageReqDto;
import com.feed02.model.wemedia.pojos.WmNews;

public interface WmNewsService extends IService<WmNews> {
    public ResponseResult findList(WmNewsPageReqDto dto);
}
