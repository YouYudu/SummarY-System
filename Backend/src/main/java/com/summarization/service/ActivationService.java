package com.summarization.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.summarization.dto.ActivationDTO;
import com.summarization.dto.ActivationPageQueryDTO;
import com.summarization.dto.FetchCommentFeedbackDTO;
import com.summarization.entity.Activation;
import com.summarization.entity.PluginFeedback;
import com.summarization.result.R;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ActivationService {
    public boolean userActivate(ActivationDTO activationDTO);

    public boolean isActivate(String activationCode);


}
