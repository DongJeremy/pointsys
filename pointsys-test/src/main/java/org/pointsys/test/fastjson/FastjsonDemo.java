package org.pointsys.test.fastjson;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class FastjsonDemo {

    public static void main(String[] args) {
        String jsonString = "[{\"version\":6,\"name\":\"Two\",\"age\":20}]";
        List<UserInfo> resultBean = JSON.parseObject(jsonString, new TypeReference<List<UserInfo>>() {
        });
        resultBean.stream().forEach(System.out::print);

    }

}
