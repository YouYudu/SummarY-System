package com.summarization;


import com.summarization.entity.Activation;
import com.summarization.mapper.ActivationMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class UserMapperTest {


    @Autowired
    private ActivationMapper activationMapper;


    void test() {
        Activation activation = new Activation();
        activation.setActivationCode("yyd_first");
        activation.setEffectiveTime(30);
        activation.setState(1);
        activationMapper.insert(activation);
    }

}