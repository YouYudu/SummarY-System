package com.summarization.service.impl;

import com.summarization.dto.CommentFeedBackDTO;
import com.summarization.dto.PluginFeedBackDTO;
import com.summarization.entity.CommentFeedback;
import com.summarization.entity.PluginFeedback;
import com.summarization.mapper.CommentFeedbackMapper;
import com.summarization.mapper.PluginFeedbackMapper;
import com.summarization.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    CommentFeedbackMapper commentFeedbackMapper;

    @Autowired
    PluginFeedbackMapper pluginFeedbackMapper;

    @Override
    public Boolean dealCommentFeedback(CommentFeedBackDTO fb) {
        try {
            CommentFeedback commentFeedback = CommentFeedback.builder()
                    .taskUuid(fb.getTaskUuid())
                    .suggestion(fb.getMessage())
                    .accuracy(fb.getScores()[0])
                    .fluency(fb.getScores()[1])
                    .informativeness(fb.getScores()[2])
                    .createTime(LocalDateTime.now())
                    .build();

            commentFeedbackMapper.insert(commentFeedback);
            return true;
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
        return false;
    }

    @Override
    public Boolean dealPluginFeedback(PluginFeedBackDTO fb) {
        try {
            PluginFeedback pluginFeedback = PluginFeedback.builder()
                    .suggestion(fb.getMessage())
                    .functionality(fb.getScores()[0])
                    .beauty(fb.getScores()[1])
                    .convenience(fb.getScores()[2])
                    .createTime(LocalDateTime.now())
                    .build();

            pluginFeedbackMapper.insert(pluginFeedback);
            return true;
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
        return false;
    }


}
