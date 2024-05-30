package com.summarization.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentGenerationDTO {
    private String code;

    private String comment;

    private String taskUuid;

    public boolean emptyComment() {
        return this.comment == null;
    }

}
