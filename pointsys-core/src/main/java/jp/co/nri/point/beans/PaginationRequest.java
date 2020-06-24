package jp.co.nri.point.beans;

import java.io.Serializable;
import java.util.Map;

public class PaginationRequest implements Serializable {

    private static final long serialVersionUID = 6159176532232364406L;

    private Integer start;
    private Integer length;
    private Map<String, Object> params;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}
