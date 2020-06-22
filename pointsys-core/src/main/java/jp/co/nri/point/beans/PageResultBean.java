package jp.co.nri.point.beans;

import java.io.Serializable;
import java.util.List;

import jp.co.nri.point.enums.ResultEnum;

public class PageResultBean implements Serializable {

    private static final long serialVersionUID = 5071118307783022228L;

    private int code;

    private String message;

    private long count;

    private List<?> data;

    public PageResultBean(long count, List<?> data) {
        this.count = count;
        this.data = data;
        this.code = ResultEnum.SUCCESS.getCode();
        this.message = ResultEnum.SUCCESS.getMessage();
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
