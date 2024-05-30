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
public class CommentTask implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String taskUuid;

    private String activationCode;

    private String code;

    private String comment;

    private LocalDateTime createTime;

}
