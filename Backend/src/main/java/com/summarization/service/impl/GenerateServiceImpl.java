package com.summarization.service.impl;

import com.summarization.Utils.KafkaUtils;
import com.summarization.Utils.RedisUtils;
import com.summarization.Utils.Tools;
import com.summarization.dto.CommentGenerationDTO;
import com.summarization.entity.CommentTask;
import com.summarization.mapper.ActivationMapper;
import com.summarization.mapper.CommentTaskMapper;
import com.summarization.service.GenerateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class GenerateServiceImpl implements GenerateService {
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private KafkaUtils kafkaUtils;

    @Autowired
    private CommentTaskMapper commentTaskMapper;

    private Integer TIME_OUT = 3000;

    @Override
    public String generateComment(String code, String activationCode) {
//        String ans = null;

        try {
//            String key = Tools.generateUUID();
            String taskUuid = Tools.generateUUID();
            String msg = String.format("%s<SEP>%s", taskUuid, code);
            kafkaUtils.send(msg);
            System.out.println(msg);
            String comment = pollRedis(taskUuid, TIME_OUT);

//            do {
//                Thread.sleep(1000);
//                String comment = (String) redisUtils.getValue(key);
//                cg.setComment(comment);
//                System.out.println(cg.getComment());
//            } while (cg.emptyComment());

            // 记录在数据库中
            CommentTask commentTask = CommentTask.builder()
                    .taskUuid(taskUuid)
                    .code(code)
                    .comment(comment)
                    .activationCode(activationCode)
                    .createTime(LocalDateTime.now())
                    .build();

            commentTaskMapper.insert(commentTask);

            return Tools.object2Json(CommentGenerationDTO.builder()
                    .comment(comment)
                    .taskUuid(taskUuid)
                    .build());
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
        return null;
    }

    private String pollRedis(String key, Integer timeout) throws InterruptedException {
        String comment = null;
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusSeconds(timeout.longValue());
        // 判断是否超时
        while (endTime.isAfter(LocalDateTime.now())) {
            // 尝试获取生成结果
            comment = (String) redisUtils.getValue(key);
            // 获取到后直接返回
            if (comment != null) {
                return comment;
            }
            // 睡眠一段时间后再获取
            Thread.sleep(1000);
        }
        // 超时后抛出异常
        throw new RuntimeException("Generation time out");
    }
}
