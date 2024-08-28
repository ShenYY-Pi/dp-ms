package com.syy.dpms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginFormDTO {
    private String phone;
    private String code;
    private String password;
}
