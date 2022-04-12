package com.mini.miniproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto {

    private boolean result;

    private String err_msg;

    private String nickname;
}
