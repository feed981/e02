package com.feed02.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feed02.model.common.dtos.ResponseResult;
import com.feed02.model.wemedia.dtos.WmLoginDto;
import com.feed02.model.wemedia.pojos.WmUser;

public interface WmUserService extends IService<WmUser> {

    /**
     * 自媒体端登录
     * @param dto
     * @return
     */
    public ResponseResult login(WmLoginDto dto);

}