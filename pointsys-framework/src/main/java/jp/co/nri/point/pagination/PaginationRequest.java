package jp.co.nri.point.pagination;

import java.io.Serializable;
import java.util.Map;

public class PaginationRequest implements Serializable {

    private static final long serialVersionUID = 6159176532232364406L;

    private Integer offset;
    private Integer limit;
    private Map<String, Object> params;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

}
