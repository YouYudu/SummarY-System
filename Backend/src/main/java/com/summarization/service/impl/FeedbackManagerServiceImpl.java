package com.summarization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.summarization.Utils.RedisUtils;
import com.summarization.dto.FetchCommentFeedbackDTO;
import com.summarization.entity.CommentFeedback;
import com.summarization.entity.CommentTask;
import com.summarization.entity.PluginFeedback;
import com.summarization.mapper.ActivationMapper;
import com.summarization.mapper.CommentFeedbackMapper;
import com.summarization.mapper.CommentTaskMapper;
import com.summarization.mapper.PluginFeedbackMapper;
import com.summarization.service.FeedbackManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FeedbackManagerServiceImpl implements FeedbackManagerService {

    @Autowired
    PluginFeedbackMapper pluginFeedbackMapper;

    @Autowired
    CommentFeedbackMapper commentFeedbackMapper;

    @Autowired
    CommentTaskMapper commentTaskMapper;

    @Autowired
    private RedisUtils redisUtils;

    private final int EXPIRATION_TIME = 3600;
    @Override
    public Page<PluginFeedback> fetchPluginFeedback(Integer pageIndex, Integer pageSize) {
        try {
            Page<PluginFeedback> page = new Page<>(pageIndex, pageSize);
            LambdaQueryWrapper<PluginFeedback> queryWrapper = new LambdaQueryWrapper<>();

            return pluginFeedbackMapper.selectPage(page, queryWrapper);
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }

        return null;
    }

    @Override
    public List<FetchCommentFeedbackDTO> fetchCommentFeedback(Integer pageIndex, Integer pageSize) {

        Page<CommentFeedback> page = new Page<>(pageIndex, pageSize);
        LambdaQueryWrapper<CommentFeedback> query1 = new LambdaQueryWrapper<>();
        Map<String, CommentFeedback> commentFeedbacks = commentFeedbackMapper.selectPage(page, query1).getRecords()
                .stream()
                .collect(Collectors.toMap(CommentFeedback::getTaskUuid, fb -> fb));


        List<String> taskList = commentFeedbacks.values()
                .stream()
                .map(CommentFeedback::getTaskUuid)
                .toList();

        LambdaQueryWrapper<CommentTask> query2 = new LambdaQueryWrapper<>();
        query2.in(CommentTask::getTaskUuid, taskList);
        List<CommentTask> commentTasks = commentTaskMapper.selectList(query2);


        List<FetchCommentFeedbackDTO> res = commentTasks.stream().map(ct -> {
            CommentFeedback fb = commentFeedbacks.get(ct.getTaskUuid());
            return FetchCommentFeedbackDTO.builder()
                    .code(ct.getCode())
                    .comment(ct.getComment())
                    .suggestion(fb.getSuggestion())
                    .accuracy(fb.getAccuracy())
                    .fluency(fb.getFluency())
                    .informativeness(fb.getInformativeness())
                    .build();
        }).toList();

        return res;
    }
}
