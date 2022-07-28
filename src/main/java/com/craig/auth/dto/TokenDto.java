package com.craig.auth.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TokenDto extends UserDto{
    private Date createTime;
}
