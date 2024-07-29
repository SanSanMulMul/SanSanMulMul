package com.sansantek.sansanmulmul.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Resource {

    private Long id;
    private String method;
    private String pattern;
    private boolean required;
}