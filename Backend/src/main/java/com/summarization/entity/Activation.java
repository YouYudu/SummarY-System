package com.summarization.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Activation implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String activationCode;

    /**
     * 0 已激活
     * 1 未激活
     * 2 停用
     */
    private Long id;

    private Integer state;

    private Integer effectiveTime;

    private LocalDateTime activateTime;

    private LocalDateTime deactivateTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
