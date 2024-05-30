package com.summarization.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.summarization.dto.ActivationDTO;
import com.summarization.dto.ActivationPageQueryDTO;
import com.summarization.entity.Activation;

public interface ActivationManagerService {
    public Page<Activation> fetchActivation(ActivationPageQueryDTO params);

    public Integer updateActivation(ActivationDTO act);

    public boolean deleteActivation(ActivationDTO act);

    public boolean addActivation(Integer num, Integer effectiveTime);
}
