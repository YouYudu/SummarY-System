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
public class PluginFeedback implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String suggestion;

    private Integer functionality;

    private Integer beauty;

    private Integer convenience;

    private LocalDateTime createTime;

}
