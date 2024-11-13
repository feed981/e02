package com.feed02.wemedia.controller.v1;

import com.feed02.model.common.dtos.ResponseResult;
import com.feed02.wemedia.service.WmChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/channel")
public class WmChannelController {

    @Autowired
    private WmChannelService wmChannelService;

    @GetMapping("channels")
    public ResponseResult fildAll(){
        return wmChannelService.fildAll();
    }
}
