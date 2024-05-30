package com.summarization.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.summarization.dto.FetchCommentFeedbackDTO;
import com.summarization.entity.PluginFeedback;

import java.util.List;

public interface FeedbackManagerService {
    public Page<PluginFeedback> fetchPluginFeedback(Integer pageIndex, Integer pageSize);

    public List<FetchCommentFeedbackDTO> fetchCommentFeedback(Integer pageIndex, Integer pageSize);
}
