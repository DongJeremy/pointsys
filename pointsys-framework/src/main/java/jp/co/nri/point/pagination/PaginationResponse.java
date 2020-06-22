package jp.co.nri.point.pagination;

import java.io.Serializable;
import java.util.List;

public class PaginationResponse implements Serializable {

    private static final long serialVersionUID = 620421858510718076L;

    private Long recordsTotal;
    private Long recordsFiltered;
    private List<?> data;

    public PaginationResponse(Long recordsTotal, Long recordsFiltered, List<?> data) {
        super();
        this.recordsTotal = recordsTotal;
        this.recordsFiltered = recordsFiltered;
        this.data = data;
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
