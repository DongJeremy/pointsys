package org.pointsys.test.jackson;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonDemo {

    public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
        String jsonString = "[{\"version\":6,\"name\":\"Two\",\"age\":20}]";
        ObjectMapper mapper = new ObjectMapper();
        List<UserInfo> resultBean = mapper.readValue(jsonString,new TypeReference<List<UserInfo>>() { });
        resultBean.stream().forEach(System.out::print);
    }

}
