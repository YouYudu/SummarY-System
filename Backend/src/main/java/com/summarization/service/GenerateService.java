package com.summarization.service;

import com.summarization.dto.CommentGenerationDTO;

public interface GenerateService {
    public String generateComment(String code, String activationCode);
}
