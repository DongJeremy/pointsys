package jp.co.nri.point.web.http;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jp.co.nri.point.beans.ResponseBean;

public class ErrorResponse extends ResponseBean {

    private static final long serialVersionUID = 1L;

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /** Downstream API name that has been called by this application */
    private DownstreamApi api;

    public ErrorResponse(RestTemplateException ex, String path) {
        super(ex.getStatusCode().value(), ex.getStatusCode().getReasonPhrase(), ex.getError(),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()), path);
        this.code = ex.getStatusCode().value();
        this.api = ex.getApi();
    }

    public DownstreamApi getApi() {
        return api;
    }

    public void setApi(DownstreamApi api) {
        this.api = api;
    }

}
