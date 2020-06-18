package jp.co.nri.point.beans;

import java.io.Serializable;

import jp.co.nri.point.enums.ResultEnum;

public class ResultBean<T> implements Serializable {

    private static final long serialVersionUID = -8276264968757808344L;

    private int code = ResultEnum.SUCCESS.getCode();

    private String error;

    private String message;

    private long timestamp;

    private String path;

    private T data;

    public ResultBean() {

    }

    public ResultBean(T data) {
        super();
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static <T> ResultBean<T> successResult(T value) {
        ResultBean<T> resultBean = new ResultBean<>();
        resultBean.setCode(ResultEnum.SUCCESS.getCode());
        resultBean.setMessage(ResultEnum.SUCCESS.getMessage());
        resultBean.setData(value);
        return resultBean;
    }

    public static ResultBean<?> successResult() {
        ResultBean<?> resultBean = new ResultBean<>();
        resultBean.setCode(ResultEnum.SUCCESS.getCode());
        resultBean.setMessage(ResultEnum.SUCCESS.getMessage());
        return resultBean;
    }

    public static ResultBean<?> errorResult() {
        ResultBean<?> resultBean = new ResultBean<>();
        resultBean.setCode(ResultEnum.ERROR.getCode());
        resultBean.setMessage(ResultEnum.ERROR.getMessage());
        return resultBean;
    }

    public static ResultBean<?> errorResult(int code, String message) {
        ResultBean<?> resultBean = new ResultBean<>();
        resultBean.setCode(code);
        resultBean.setMessage(message);
        return resultBean;
    }

    public static ResultBean<?> errorResult(String message) {
        ResultBean<?> resultBean = new ResultBean<>();
        resultBean.setCode(ResultEnum.ERROR.getCode());
        resultBean.setMessage(message);
        return resultBean;
    }

}
