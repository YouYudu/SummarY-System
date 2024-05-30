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
public class CommentFeedback implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String taskUuid;

    private String suggestion;

    private Integer accuracy;

    private Integer fluency;

    private Integer informativeness;

    private LocalDateTime createTime;

}
