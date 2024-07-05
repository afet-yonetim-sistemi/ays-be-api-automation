package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Pageable {

    private int page;
    private int pageSize;
    private List<Orders> orders;

    public static Pageable generateFirstPage() {
        Pageable pageable = new Pageable();
        pageable.setPage(1);
        pageable.setPageSize(10);
        return pageable;
    }

    public static Pageable generate(int page, int pageSize) {
        Pageable pageable = new Pageable();
        pageable.setPageSize(pageSize);
        pageable.setPage(page);
        pageable.setOrders(Orders.generate("createdAt", "DESC"));
        return pageable;
    }

}
