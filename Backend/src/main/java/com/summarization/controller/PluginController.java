package com.summarization.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.summarization.Utils.Tools;
import com.summarization.annotation.IgnoreActivation;
import com.summarization.dto.ActivationDTO;
import com.summarization.dto.CommentFeedBackDTO;
import com.summarization.dto.CommentGenerationDTO;
import com.summarization.dto.PluginFeedBackDTO;
import com.summarization.entity.Activation;
import com.summarization.enumeration.ActivationStatusEnum;
import com.summarization.mapper.ActivationMapper;
import com.summarization.result.R;
import com.summarization.service.ActivationService;
import com.summarization.service.FeedbackService;
import com.summarization.service.GenerateService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;


@RestController
public class PluginController {
    @Resource
    private FeedbackService feedbackService;
    @Resource
    private GenerateService generateService;
    @Resource
    private ActivationService activationService;

    @Autowired
    private ActivationMapper activationMapper;

    @IgnoreActivation
    @GetMapping("/plugin/hello")
    public ResponseEntity<Object> hello(){
        String result = "hello yyd SummarY";
//        LambdaQueryWrapper<Activation> query = new LambdaQueryWrapper<>();
//        query.eq(Activation::getActivationCode, "yyd_third");
//        Activation act = activationMapper.selectOne(query);
//        act.setState(ActivationStatusEnum.DISABLE.getState());
//        activationMapper.updateById(act);

        return ResponseEntity.status(200).body(result);
    }

    @PostMapping("/plugin/commentFeedback")
    public R<String> commentFeedback(@RequestBody CommentFeedBackDTO fb) {
        String result = "CommentFeedback SummarY";
        System.out.println(result + fb);

        feedbackService.dealCommentFeedback(fb);

        return R.success();
    }

    @PostMapping("/plugin/pluginFeedback")
    public R<String> pluginFeedback(@RequestBody PluginFeedBackDTO fb) {
        String result = "PluginFeedback SummarY";
        System.out.println(result);

        feedbackService.dealPluginFeedback(fb);

        return R.success();
    }

//    @IgnoreActivation
    @PostMapping("/plugin/generateComment")
    public R<String> generateComment(@RequestBody CommentGenerationDTO cg, @RequestHeader(value = "ActivationCode") String activationCode) {
        String result = "Generate code comment";
        System.out.println(result +'\n'+ activationCode +'\n'+ cg);
        long startTime = System.currentTimeMillis();

        String res = generateService.generateComment(cg.getCode(), activationCode);

        // 获取程序结束时间
        long endTime = System.currentTimeMillis();

        // 计算程序运行时间
        long duration = endTime - startTime;

        System.out.println("程序运行时间： " + duration + " 毫秒");

        return R.success(res);
    }

    @IgnoreActivation
    @PostMapping("/plugin/activate")
    public R<String> activate(@RequestBody ActivationDTO activationDTO) {
        String result = "Activate";
        System.out.println(result + activationDTO.getActivationCode());

        boolean flag = activationService.userActivate(activationDTO);

        if (flag)
            return R.success();

        return R.error("激活失败");
    }
}
