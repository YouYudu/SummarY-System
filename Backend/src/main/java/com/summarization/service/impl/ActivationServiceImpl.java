package com.summarization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.summarization.Utils.RedisUtils;
import com.summarization.Utils.Tools;
import com.summarization.dto.*;
import com.summarization.entity.Activation;
import com.summarization.entity.CommentFeedback;
import com.summarization.entity.CommentTask;
import com.summarization.entity.PluginFeedback;
import com.summarization.enumeration.ActivationStatusEnum;
import com.summarization.mapper.ActivationMapper;
import com.summarization.mapper.CommentFeedbackMapper;
import com.summarization.mapper.CommentTaskMapper;
import com.summarization.mapper.PluginFeedbackMapper;
import com.summarization.result.R;
import com.summarization.service.ActivationService;
import com.summarization.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ActivationServiceImpl implements ActivationService {

    @Autowired
    ActivationMapper activationMapper;


    @Autowired
    private RedisUtils redisUtils;

    private final int EXPIRATION_TIME = 3600;

    @Override
    public boolean userActivate(ActivationDTO activationDTO) {
        try {
            String actCode = activationDTO.getActivationCode();
            Integer cacheState = (Integer) redisUtils.getValue(actCode);

            // 缓存中没查到或者查到的是未激活，执行数据库流程
            if (cacheState == null || cacheState.equals(ActivationStatusEnum.NOT_ACTIVE.getState())) {
                LambdaQueryWrapper<Activation> query = new LambdaQueryWrapper<>();
                query.eq(Activation::getActivationCode, actCode);
                Activation act = activationMapper.selectOne(query);


                if (act == null || act.getState().equals(ActivationStatusEnum.DISABLE.getState())) {
                    // 若数据库中不存在该激活码，或该激活码已经停用，则激活失败
                    redisUtils.cacheValue(actCode, ActivationStatusEnum.DISABLE.getState(), EXPIRATION_TIME);
                    return false;
                } else if (act.getState().equals(ActivationStatusEnum.NOT_ACTIVE.getState())) {
                    // 若激活码处于未激活状态，执行激活操作
                    LocalDateTime now = LocalDateTime.now();
                    act.setState(ActivationStatusEnum.ACTIVE.getState());
                    act.setActivateTime(now);
                    act.setUpdateTime(now);
                    act.setDeactivateTime(now.plusDays(act.getEffectiveTime()));
                    activationMapper.updateById(act);
                    redisUtils.cacheValue(actCode, act.getState(), EXPIRATION_TIME);
                    return true;
                } else if (act.getDeactivateTime().isBefore(LocalDateTime.now())) {
                    // 若激活码处于失效期，对激活码的状态进行更新和缓存
                    act.setState(ActivationStatusEnum.DISABLE.getState());
                    activationMapper.updateById(act);
                    redisUtils.cacheValue(actCode, ActivationStatusEnum.DISABLE.getState(), EXPIRATION_TIME);
                    return false;
                }

                return true;
            }

            return cacheState.equals(ActivationStatusEnum.ACTIVE.getState());
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }

        return false;
    }

    @Override
    public boolean isActivate(String activationCode) {
        try {
            Integer cacheState = (Integer) redisUtils.getValue(activationCode);

            // 缓存没查到相关信息
            if (cacheState == null) {
                LambdaQueryWrapper<Activation> query = new LambdaQueryWrapper<>();
                query.eq(Activation::getActivationCode, activationCode);
                Activation act = activationMapper.selectOne(query);

                // 不是激活状态
                if (act == null || !act.getState().equals(ActivationStatusEnum.ACTIVE.getState())) {
                    redisUtils.cacheValue(activationCode, ActivationStatusEnum.DISABLE.getState(), EXPIRATION_TIME);
                    return false;
                }

                // 若已经处于失效期，修改状态
                if (act.getDeactivateTime().isBefore(LocalDateTime.now())) {
                    act.setState(ActivationStatusEnum.DISABLE.getState());
                    activationMapper.updateById(act);
                    redisUtils.cacheValue(activationCode, ActivationStatusEnum.DISABLE.getState(), EXPIRATION_TIME);
                    return false;
                }

                // 是激活状态
                if (act.getState().equals(ActivationStatusEnum.ACTIVE.getState())) {
                    redisUtils.cacheValue(activationCode, ActivationStatusEnum.ACTIVE.getState(), EXPIRATION_TIME);
                    return true;
                }
            }


            return cacheState.equals(ActivationStatusEnum.ACTIVE.getState());

        } catch (Exception e) {
            log.error(String.valueOf(e));
        }

        return false;
    }



}
