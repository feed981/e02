package com.feed02.model.user.dtos;

import lombok.Data;

@Data
public class LoginDto {
    private String phone; // 手机号
    private String password; // 密码
}
