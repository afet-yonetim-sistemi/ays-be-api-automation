package org.ays.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Orders {

    private String property;
    private String direction;

    public static List<Orders> generate(String property, String direction) {
        Orders orders = new Orders();
        orders.setDirection(direction);
        orders.setProperty(property);
        List<Orders> ordersList = Arrays.asList(orders);
        return ordersList;
    }

}
