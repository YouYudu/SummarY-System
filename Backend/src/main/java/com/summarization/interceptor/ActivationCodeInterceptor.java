package com.summarization.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.summarization.annotation.IgnoreActivation;
import com.summarization.entity.Activation;
import com.summarization.mapper.ActivationMapper;
import com.summarization.service.ActivationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;



@Component
@Slf4j
public class ActivationCodeInterceptor implements HandlerInterceptor {
    @Autowired
    ActivationService activationService;


    /**
     * 客户端激活码校验
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截到了请求:{}", request.getRequestURI());
//        response.setContentType("text/plain;charset=utf-8");

        if (!(handler instanceof HandlerMethod)) {
            //如果不是映射到controller某个方法的请求，则直接放行，例如请求的是/doc.html
            return true;
        }

        //判断当前被拦截的Controller方法上是否加入了IgonreToken注解
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        //加入了IgnoreToken注解，直接放行
        boolean hasMethodAnnotation = handlerMethod.hasMethodAnnotation(IgnoreActivation.class);
        if (hasMethodAnnotation) {
            return true;
        }

        //从请求头获取jwt令牌
        String activationCode = request.getHeader("ActivationCode");

        try {
            System.out.println(activationCode);
            if (activationCode == null || !activationService.isActivate(activationCode)) {
                response.setStatus(401);
                return false;
            }
        } catch (Exception e) {
            log.info("激活码解析失败");
            response.setStatus(401);
            return false;
        }
        return true;
    }



}
