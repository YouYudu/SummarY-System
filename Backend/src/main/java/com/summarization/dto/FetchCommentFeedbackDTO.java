package com.summarization.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FetchCommentFeedbackDTO {
    private String code;
    private String comment;
    private String suggestion;
    private Integer accuracy;
    private Integer fluency;
    private Integer informativeness;
}
