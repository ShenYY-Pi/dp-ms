package com.syy.dpms;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RedisData {
    private LocalDateTime expireTime;
    private Object data;
}
