package com.summarization.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.summarization.Utils.Tools;
import com.summarization.dto.ActivationDTO;
import com.summarization.dto.ActivationPageQueryDTO;
import com.summarization.dto.FetchCommentFeedbackDTO;
import com.summarization.entity.Activation;
import com.summarization.entity.Admin;
import com.summarization.entity.PluginFeedback;
import com.summarization.mapper.ActivationMapper;
import com.summarization.result.PageResult;
import com.summarization.result.R;
import com.summarization.service.*;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private FeedbackService feedbackService;
    @Resource
    private GenerateService generateService;
    @Resource
    private ActivationService activationService;
    @Resource
    private ActivationManagerService activationManagerService;

    @Resource
    private FeedbackManagerService feedbackManagerService;

    @Resource
    private LoginService loginService;

    @Autowired
    private ActivationMapper activationMapper;


    @PostMapping ("/fetchActivation")
    public R<Page> fetchActivation(ActivationPageQueryDTO params) {
        Page<Activation> activationPage = activationManagerService.fetchActivation(params);

        return R.success(activationPage);
    }

    @PostMapping ("/updateActivation")
    public R<Integer> updateActivation(@RequestBody ActivationDTO act) {
        Integer leftTime = activationManagerService.updateActivation(act);

        return R.success(leftTime);
    }

    @PostMapping ("/deleteActivation")
    public R<Boolean> deleteActivation(@RequestBody ActivationDTO act) {
        boolean flag = activationManagerService.deleteActivation(act);

        return R.success(flag);
    }

    @GetMapping ("/addActivation")
    public R<Boolean> addActivation(Integer num, Integer effectiveTime) {
        boolean flag = activationManagerService.addActivation(num, effectiveTime);

        return R.success(flag);
    }


    @GetMapping ("/fetchPluginFeedback")
    public R<Page> fetchPluginFeedback(Integer pageIndex, Integer pageSize) {
        Page<PluginFeedback> activationPage = feedbackManagerService.fetchPluginFeedback(pageIndex, pageSize);

        return R.success(activationPage);
    }


    @GetMapping ("/fetchCommentFeedback")
    public R<PageResult> fetchCommentFeedback(Integer pageIndex, Integer pageSize) {
        List<FetchCommentFeedbackDTO> res = feedbackManagerService.fetchCommentFeedback(pageIndex, pageSize);

        return R.success(PageResult.builder().records(res).total(res.size()).build());
    }

    @PostMapping ("/login")
    public R<Boolean> login(String username, String password) {
        boolean flag = loginService.login(username, password);

        return R.success(flag);
    }

    @PostMapping ("/adminEdit")
    public R<Boolean> adminEdit(String username, String oldPassword, String newPassword) {
        boolean flag = loginService.changPassword(username, oldPassword, newPassword);

        return R.success(flag);
    }

}
