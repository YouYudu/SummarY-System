package com.summarization.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class ActivationPageQueryDTO implements Serializable {
    private String activationCode;
    private Integer state;
    private Integer pageIndex;
    private Integer pageSize;
}
