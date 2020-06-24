package jp.co.nri.point.beans;

import java.io.Serializable;
import java.util.List;

import jp.co.nri.point.enums.ResultEnum;

public class PageResultBean extends PaginationResponse {

    private static final long serialVersionUID = 5071118307783022228L;

    private int code;

    private String message;

    private long recordsTotal;

    private long recordsFiltered;

    private List<?> data;

    public PageResultBean(long count, List<?> data) {
        this.recordsTotal = count;
        this.recordsFiltered = count;
        this.code = ResultEnum.SUCCESS.getCode();
        this.message = ResultEnum.SUCCESS.getMessage();
    }

    public PageResultBean(long recordsTotal, long recordsFiltered, List<?> data) {
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.code = ResultEnum.SUCCESS.getCode();
        this.message = ResultEnum.SUCCESS.getMessage();
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
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
