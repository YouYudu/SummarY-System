package com.summarization.service;

import com.summarization.dto.CommentFeedBackDTO;
import com.summarization.dto.PluginFeedBackDTO;

public interface FeedbackService {
    public Boolean dealCommentFeedback(CommentFeedBackDTO fb);

    public Boolean dealPluginFeedback(PluginFeedBackDTO fb);
}
