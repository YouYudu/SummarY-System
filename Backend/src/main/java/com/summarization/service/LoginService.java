package com.summarization.service;

import com.summarization.dto.CommentFeedBackDTO;
import com.summarization.dto.PluginFeedBackDTO;

public interface LoginService {
    public Boolean login(String username, String password);

    public Boolean changPassword(String username, String oldPassword, String newPassword);
}
