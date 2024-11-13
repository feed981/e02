package com.feed02.wemedia.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.feed02.model.common.dtos.ResponseResult;
import com.feed02.model.wemedia.pojos.WmChannel;
import com.feed02.wemedia.mapper.WmChannelMapper;
import com.feed02.wemedia.service.WmChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class WmChannelServiceImpl extends ServiceImpl<WmChannelMapper , WmChannel> implements WmChannelService {
    @Override
    public ResponseResult fildAll() {
        return ResponseResult.okResult(list());
    }
}
