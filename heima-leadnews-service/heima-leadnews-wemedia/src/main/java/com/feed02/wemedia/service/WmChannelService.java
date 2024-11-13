package com.feed02.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feed02.model.common.dtos.ResponseResult;
import com.feed02.model.wemedia.pojos.WmChannel;

public interface WmChannelService extends IService<WmChannel> {
    // 查询所有频道
    public ResponseResult fildAll();
}
