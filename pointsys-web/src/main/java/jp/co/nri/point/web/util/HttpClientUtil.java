package jp.co.nri.point.web.util;

import java.util.Arrays;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import jp.co.nri.point.beans.PageResultBean;
import jp.co.nri.point.beans.ResultBean;
import jp.co.nri.point.constant.Constants;

public class HttpClientUtil {

    private static HttpEntity<String> generateEmptyEntity(String token) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(Constants.TOKEN_KEY, token);
        HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
        return requestEntity;
    }

    private static <T> HttpEntity<T> generateDataEntity(String token, T entity) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(Constants.TOKEN_KEY, token);
        HttpEntity<T> requestEntity = new HttpEntity<T>(entity, requestHeaders);
        return requestEntity;
    }
    
    private static <T> HttpEntity<T> generateFileEntity(String token) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(Constants.TOKEN_KEY, token);
        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
        HttpEntity<T> requestEntity = new HttpEntity<T>(null, requestHeaders);
        return requestEntity;
    }

    /**
     * 
     * @param <T>
     * @param restTemplate
     * @param token
     * @param url
     * @param responseType
     * @return
     */
    public static <T> T doGet(RestTemplate restTemplate, String token, String url, Class<T> responseType) {
        HttpEntity<String> requestEntity = generateEmptyEntity(token);
        ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
        return result.getBody();
    }

    public static <T> T doGetFile(RestTemplate restTemplate, String token, String url, Class<T> responseType) {
        HttpEntity<String> requestEntity = generateFileEntity(token);
        ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
        return result.getBody();
    }

    /**
     * 
     * @param <T>
     * @param restTemplate
     * @param token
     * @param url
     * @param responseType
     * @return
     */
    public static <T> T doGet(RestTemplate restTemplate, String token, String url,
            ParameterizedTypeReference<T> responseType) {
        HttpEntity<String> requestEntity = generateEmptyEntity(token);
        ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
        return result.getBody();
    }

    /**
     * 
     * @param <T>
     * @param restTemplate
     * @param token
     * @param url
     * @param responseType
     * @return
     */
    public static <T> ResultBean<T> doGetResultBean(RestTemplate restTemplate, String token, String url,
            Class<T> responseType) {
        HttpEntity<String> requestEntity = generateEmptyEntity(token);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        return JSON.parseObject(result.getBody(), new TypeReference<ResultBean<T>>() {
        });
    }

    /**
     * 
     * @param <T>
     * @param restTemplate
     * @param url
     * @param token
     * @param parmas
     * @param responseType
     * @return
     */
    public static <T> PageResultBean<T> doGetPageResultBean(RestTemplate restTemplate, String token, String url,
            MultiValueMap<String, String> parmas, Class<T> responseType) {
        HttpEntity<String> requestEntity = generateEmptyEntity(token);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(parmas);
        ResponseEntity<String> result = restTemplate.exchange(builder.build().toUriString(), HttpMethod.GET,
                requestEntity, String.class, parmas);
        return JSON.parseObject(result.getBody(), new TypeReference<PageResultBean<T>>() {
        });
    }

    /**
     * 
     * @param <T>
     * @param restTemplate
     * @param url
     * @param entity
     * @param responseType
     * @return
     */
    public static <T> T doPost(RestTemplate restTemplate, String url, Object entity, Class<T> responseType) {
        ResponseEntity<T> responseEntity = restTemplate.postForEntity(url, entity, responseType);
        return responseEntity.getBody();
    }

    /**
     * 
     * @param <T>
     * @param restTemplate
     * @param token
     * @param url
     * @param entity
     * @param responseType
     * @return
     */
    public static <T> T doPost(RestTemplate restTemplate, String token, String url, T entity, Class<T> responseType) {
        HttpEntity<T> requestEntity = generateDataEntity(token, entity);
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
        return responseEntity.getBody();
    }

    public static <T> ResultBean<T> doPostResultBean(RestTemplate restTemplate, String token, String url, T entity,
            Class<T> responseType) {
        HttpEntity<T> requestEntity = generateDataEntity(token, entity);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return JSON.parseObject(result.getBody(), new TypeReference<ResultBean<T>>() {
        });
    }

    /**
     * 
     * @param <T>
     * @param restTemplate
     * @param token
     * @param url
     * @param entity
     * @param responseType
     * @return
     */
    public static <T> ResultBean<T> doDeleteResultBean(RestTemplate restTemplate, String token, String url,
            Class<T> responseType) {
        HttpEntity<String> requestEntity = generateEmptyEntity(token);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);
        return JSON.parseObject(result.getBody(), new TypeReference<ResultBean<T>>() {
        });
    }

    /**
     * 
     * @param <T>
     * @param restTemplate
     * @param token
     * @param url
     * @param entity
     * @param responseType
     * @return
     */
    public static <T> ResultBean<T> doUpdateResultBean(RestTemplate restTemplate, String token, String url, T entity,
            Class<T> responseType) {
        HttpEntity<T> requestEntity = generateDataEntity(token, entity);
        ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
        return JSON.parseObject(result.getBody(), new TypeReference<ResultBean<T>>() {
        });
    }

}
