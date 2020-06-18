package jp.co.nri.point.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResponseBean implements Serializable {

    private static final long serialVersionUID = -4417715614021482064L;

    @JsonIgnore
    private int status;

    private String error;

    private String message;

    private String timestamp;

    private String path;

    public ResponseBean() {
        super();
    }

    public ResponseBean(int status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public ResponseBean(int status, String error, String message, String timestamp, String path) {
        super();
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
