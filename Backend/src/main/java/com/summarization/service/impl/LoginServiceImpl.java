package com.summarization.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.summarization.Utils.RedisUtils;
import com.summarization.entity.Activation;
import com.summarization.entity.Admin;
import com.summarization.mapper.AdminMapper;
import com.summarization.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private AdminMapper adminMapper;

    private final int EXPIRATION_TIME = 3600;
    @Override
    public Boolean login(String username, String password) {
        try {
            // 查看Redis中是否缓存了该账号的密码
            String realPassword = (String) redisUtils.getValue(username);
            // 如果Redis中没有，则从数据库中查询该账号的密码
            if (realPassword == null) {
                LambdaQueryWrapper<Admin> query = new LambdaQueryWrapper<>();
                query.eq(Admin::getUsername, username);
                Admin admin = adminMapper.selectOne(query);
                realPassword = admin.getPassword();
                // 缓存真实密码
                redisUtils.cacheValue(username, realPassword, EXPIRATION_TIME);
            }
            // 对比真实密码与用户输入的密码
            return password.equals(realPassword);
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }

        return false;
    }

    @Override
    public Boolean changPassword(String username, String oldPassword, String newPassword) {
        try {
            LambdaQueryWrapper<Admin> query = new LambdaQueryWrapper<>();
            query.eq(Admin::getUsername, username);
            Admin admin = adminMapper.selectOne(query);
            String realPassword = admin.getPassword();
            // 如果用户输入的旧密码与真实密码相同
            if (oldPassword.equals(realPassword)) {
                // 更新数据库中的密码
                admin.setPassword(newPassword);
                adminMapper.updateById(admin);
                // 缓存真实密码
                redisUtils.cacheValue(username, newPassword, EXPIRATION_TIME);
                // 更改成功
                return true;
            }
            // 更改失败
            return false;
        } catch (Exception e) {
            log.error(String.valueOf(e));
        }
        return false;
    }
}
