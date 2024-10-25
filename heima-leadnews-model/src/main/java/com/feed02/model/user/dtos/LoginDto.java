package com.feed02.model.user.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginDto {
    @ApiModelProperty(value = "手机号" ,required = true)
    private String phone; // 手机号
    @ApiModelProperty(value = "密码" ,required = true)
    private String password; // 密码
}
