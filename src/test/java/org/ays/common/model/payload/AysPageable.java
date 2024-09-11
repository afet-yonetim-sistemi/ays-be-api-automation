package org.ays.common.model.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.payload.AysOrder;

import java.util.List;

@Getter
@Setter
public class AysPageable {

    private int page;
    private int pageSize;
    private List<AysOrder> orders;

    public static AysPageable generateFirstPage() {
        AysPageable pageable = new AysPageable();
        pageable.setPage(1);
        pageable.setPageSize(10);
        return pageable;
    }

    public static AysPageable generate(int page, int pageSize) {
        AysPageable pageable = new AysPageable();
        pageable.setPageSize(pageSize);
        pageable.setPage(page);
        pageable.setOrders(AysOrder.generate("createdAt", "DESC"));
        return pageable;
    }

}
