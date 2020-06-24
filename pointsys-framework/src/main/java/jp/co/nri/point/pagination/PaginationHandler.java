package jp.co.nri.point.pagination;

import java.util.ArrayList;
import java.util.List;

import jp.co.nri.point.beans.PaginationRequest;
import jp.co.nri.point.beans.PaginationResponse;

public class PaginationHandler {

    private CountHandler countHandler;
    private ListHandler listHandler;
    private OrderHandler orderHandler;

    public PaginationHandler(CountHandler countHandler, ListHandler listHandler) {
        super();
        this.countHandler = countHandler;
        this.listHandler = listHandler;
    }

    public PaginationHandler(CountHandler countHandler, ListHandler listHandler, OrderHandler orderHandler) {
        this(countHandler, listHandler);
        this.orderHandler = orderHandler;
    }

    public PaginationResponse handle(PaginationRequest dtRequest) {
        long count = 0;
        List<?> list = null;

        count = this.countHandler.count(dtRequest);
        if (count > 0) {
            if (orderHandler != null) {
                dtRequest = orderHandler.order(dtRequest);
            }
            list = this.listHandler.list(dtRequest);
        }

        if (list == null) {
            list = new ArrayList<>();
        }

        return new PaginationResponse(count, count, list);
    }

    public interface ListHandler {
        List<?> list(PaginationRequest request);
    }

    public interface CountHandler {
        long count(PaginationRequest request);
    }

    public interface OrderHandler {
        PaginationRequest order(PaginationRequest request);
    }

}
