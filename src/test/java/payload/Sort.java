package payload;

import java.util.List;

public class Sort {

    private List<Sort> sort;
    private String property;
    private String direction;

    public List<Sort> getSort() {
        return sort;
    }
    public void setSort(List<Sort> sort) {
        this.sort = sort;
    }
    public String getProperty() {
        return property;
    }
    public void setProperty(String property) {
        this.property = property;
    }
    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }


}
