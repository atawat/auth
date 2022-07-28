package com.craig.auth.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> String toJson(T obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    public static <T> T parse(String json, Class<T> tClass) throws IOException {
        return mapper.readValue(json, tClass);
    }

    public static <T> T parse(String json, TypeReference tClass) throws IOException {
        return mapper.readValue(json, tClass);
    }
}
