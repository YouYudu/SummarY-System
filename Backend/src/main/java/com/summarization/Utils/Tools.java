package com.summarization.Utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.util.UUID;

public class Tools {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();

    public static String object2Json(Object obj) throws JsonProcessingException {
//        OBJECT_MAPPER.findAndRegisterModules();
        return OBJECT_MAPPER.writeValueAsString(obj);
    }

    public static <T> T json2Obj(String str, Class<T> clazz) throws JsonProcessingException {
        if (StringUtils.isEmpty(str) || clazz == null){
            return null;
        }
        return clazz.equals(String.class) ? (T) str : OBJECT_MAPPER.readValue(str,clazz);
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
