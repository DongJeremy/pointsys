package jp.co.nri.point.beans;

import java.io.Serializable;
import java.util.List;

import jp.co.nri.point.enums.ResultEnum;

public class PaginationResponse implements Serializable {

    private static final long serialVersionUID = 620421858510718076L;

    private int code;
    private String message;
    private Long recordsTotal;
    private Long recordsFiltered;
    private List<?> data;

    public PaginationResponse(long recordsTotal, long recordsFiltered, List<?> data) {
        super();
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.data = data;
        this.code = ResultEnum.SUCCESS.getCode();
        this.message = ResultEnum.SUCCESS.getMessage();
    }
    
    public PaginationResponse(long count, List<?> data) {
        this.recordsTotal = count;
        this.recordsFiltered = count;
        this.code = ResultEnum.SUCCESS.getCode();
        this.message = ResultEnum.SUCCESS.getMessage();
    }
    
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

}
