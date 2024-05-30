package com.summarization.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ActivationStatusEnum {

    ACTIVE(0, "已激活"),
    NOT_ACTIVE(1, "未激活"),
    DISABLE(2,"停用");

    private final Integer state;
    private final String desc;
}
