package jp.co.nri.point.web.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.nri.point.beans.ResponseBean;

public class RestApiTemplateErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
                String httpBodyResponse = reader.lines().collect(Collectors.joining(""));

                ObjectMapper objectMapper = new ObjectMapper();
                ResponseBean responseBean = objectMapper.readValue(httpBodyResponse, ResponseBean.class);
                String errorMessage = responseBean.getMessage();

                throw new RestTemplateException(DownstreamApi.HTTP_CLIENT, response.getStatusCode(), errorMessage);
            }
        }
    }
}
