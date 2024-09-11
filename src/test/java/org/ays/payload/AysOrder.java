package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AysOrder {

    private String property;
    private String direction;

    public static List<AysOrder> generate(String property, String direction) {
        AysOrder orders = new AysOrder();
        orders.setDirection(direction);
        orders.setProperty(property);
        return List.of(orders);
    }

}
