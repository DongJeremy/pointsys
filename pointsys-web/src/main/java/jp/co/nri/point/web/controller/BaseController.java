package jp.co.nri.point.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import jp.co.nri.point.constant.Constants;
import jp.co.nri.point.web.config.HttpClientProperties;

public class BaseController {

    @Autowired
    protected HttpClientProperties httpClientProperties;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected HttpServletRequest request;

    protected String getUrlString(String url) {
        return httpClientProperties.getUrl() + url;
    }
    
    protected String getTokenString() {
        return request.getHeader(Constants.TOKEN_KEY);
    }
}
