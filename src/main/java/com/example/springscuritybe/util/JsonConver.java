package com.example.springscuritybe.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConver {
    public static String convertStringToJson(Object ob) throws JsonProcessingException {
        try {
            return new ObjectMapper().writeValueAsString(ob);
        } catch (JsonProcessingException jpe){
            return null;
        }
    }
}
