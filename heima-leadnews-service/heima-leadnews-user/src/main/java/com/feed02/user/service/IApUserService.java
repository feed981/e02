package com.feed02.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.feed02.model.common.dtos.ResponseResult;
import com.feed02.model.user.dtos.LoginDto;
import com.feed02.model.user.pojos.ApUser;

public interface IApUserService extends IService<ApUser> {
    // app端登陆功能
    public ResponseResult login(LoginDto dto);
}
