package jp.co.nri.point.web.http;

import org.springframework.http.HttpStatus;

public class RestTemplateException extends RuntimeException {

    private static final long serialVersionUID = -5062931411787268883L;
    private DownstreamApi api;
    private HttpStatus statusCode;
    private String error;

    public RestTemplateException(DownstreamApi api, HttpStatus statusCode, String error) {
        super(error);
        this.api = api;
        this.statusCode = statusCode;
        this.error = error;
    }

    public DownstreamApi getApi() {
        return api;
    }

    public void setApi(DownstreamApi api) {
        this.api = api;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
