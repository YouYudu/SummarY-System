package com.summarization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.summarization.Utils.RedisUtils;
import com.summarization.Utils.Tools;
import com.summarization.dto.ActivationDTO;
import com.summarization.dto.ActivationPageQueryDTO;
import com.summarization.entity.Activation;
import com.summarization.enumeration.ActivationStatusEnum;
import com.summarization.mapper.ActivationMapper;
import com.summarization.mapper.CommentFeedbackMapper;
import com.summarization.mapper.CommentTaskMapper;
import com.summarization.mapper.PluginFeedbackMapper;
import com.summarization.service.ActivationManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class ActivationManagerServiceImpl implements ActivationManagerService {
    @Autowired
    ActivationMapper activationMapper;

    @Autowired
    private RedisUtils redisUtils;

    private final int EXPIRATION_TIME = 3600;
    @Override
    public Page<Activation> fetchActivation(ActivationPageQueryDTO params) {
        try {
            Page<Activation> page = new Page<>(params.getPageIndex(), params.getPageSize());
            LambdaQueryWrapper<Activation> queryWrapper = new LambdaQueryWrapper<>();

            if (params.getActivationCode() != null && !params.getActivationCode().isEmpty()) {
                queryWrapper.eq(Activation::getActivationCode, params.getActivationCode());
            }

            if (params.getState() != null) {
                queryWrapper.eq(Activation::getState, params.getState());
            }

            Page<Activation> activations = activationMapper.selectPage(page, queryWrapper);

            activations.getRecords().forEach(activation -> {
                if (activation.getActivateTime() != null) {
                    Duration duration = Duration.between(LocalDateTime.now(), activation.getDeactivateTime());
                    int effectiveTime = (int) duration.toDays();
                    activation.setEffectiveTime(Math.max(effectiveTime, 0));
                }
            });

            return activations;
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }

        return null;
    }

    @Override
    public Integer updateActivation(ActivationDTO act) {
        try {
            LambdaQueryWrapper<Activation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Activation::getActivationCode, act.getActivationCode());

            Activation activation = activationMapper.selectOne(queryWrapper);
            activation.setUpdateTime(LocalDateTime.now());
            activation.setDeactivateTime(act.getDeactivateTime());
            activation.setState(act.getState());

            Duration duration = Duration.between(LocalDateTime.now(), activation.getDeactivateTime());
            Integer leftTime = (int) duration.toDays();
            activation.setEffectiveTime(leftTime);

            activationMapper.updateById(activation);
            redisUtils.cacheValue(act.getActivationCode(), act.getState(), EXPIRATION_TIME);

            return leftTime;
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }

        return null;
    }

    @Override
    public boolean deleteActivation(ActivationDTO act) {
        try {
            LambdaQueryWrapper<Activation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Activation::getActivationCode, act.getActivationCode());

            int delete = activationMapper.delete(queryWrapper);
            if (delete > 0) {
                return true;
            }
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }

        return false;
    }

    @Override
    public boolean addActivation(Integer num, Integer time) {
        LocalDateTime now = LocalDateTime.now();

        List<Activation> activationList = new ArrayList<>();

        while (num-- != 0) {
            activationList.add(Activation.builder()
                    .activationCode(Tools.generateUUID())
                    .state(ActivationStatusEnum.NOT_ACTIVE.getState())
                    .effectiveTime(time)
                    .createTime(now)
                    .updateTime(now).build());
        }

        activationMapper.insertBatchSomeColumn(activationList);


        return true;
    }


}
