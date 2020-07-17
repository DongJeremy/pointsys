package org.pointsys.test.jackson;

import java.util.List;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JacksonDemo {

    public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
        String jsonString = "[{\"version\":6,\"name\":\"Two\",\"age\":20},{\"version\":7,\"name\":\"One\",\"age\":10}]";
        ObjectMapper mapper = new ObjectMapper();
        List<UserInfo> resultBean = mapper.readValue(jsonString, new TypeReference<List<UserInfo>>() {
        });
        resultBean.stream().forEach(System.out::print);
        String carString = "{\"id\":1, \"data\": {\"version\":6,\"name\":\"Two\",\"age\":20}, \"color\": \"blue\"}";
        Car<UserInfo> carBean = mapper.readValue(carString, new TypeReference<Car<UserInfo>>() {
        });
        System.out.println(carBean);

        String jsonString2 = "{\"companyCode\": \"a\", \"code\":0, \"data\": {\"version\":6,\"name\":\"Two\",\"age\":20}}";

        BaseResponseDto<UserInfo> resp = mapper.readValue(jsonString2, new TypeReference<BaseResponseDto<UserInfo>>() {
        });
        System.out.println(resp.getData());

        BaseResponseDto<UserInfo> userInfo = parseBaseResponse(jsonString2, UserInfo.class);
        System.out.println(userInfo.getData());

    }

    public static <T> BaseResponseDto<T> parseBaseResponse(final String str, Class<T> clazz)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        if (StringUtils.isEmpty(str) || null == clazz) {
            return null;
        }
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructParametricType(BaseResponseDto.class, clazz);
        return objectMapper.readValue(str, javaType);
    }

    public static <T> List<T> parseList(final String str, Class<T> clazz)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        if (StringUtils.isEmpty(str) || null == clazz) {
            return null;
        }
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        return objectMapper.readValue(str, typeFactory.constructParametricType(List.class, clazz));
    }

}
