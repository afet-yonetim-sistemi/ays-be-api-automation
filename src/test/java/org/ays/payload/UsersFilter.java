package org.ays.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.entity.UserEntity;
import org.ays.common.model.payload.AysPhoneNumber;

import java.util.List;

@Getter
@Setter
public class UsersFilter {

    private String firstName;
    private String lastName;
    private String city;
    private List<String> statuses;
    private AysPhoneNumber phoneNumber;

    public static UsersFilter generate(AysPhoneNumber phoneNumber,
                                       String firstname,
                                       String lastname,
                                       String city,
                                       List<String> statuses) {

        UsersFilter filters = new UsersFilter();
        filters.setPhoneNumber(phoneNumber);
        filters.setFirstName(firstname);
        filters.setLastName(lastname);
        filters.setCity(city);
        filters.setStatuses(statuses);
        return filters;
    }

    public static UsersFilter from(UserEntity userEntity) {
        UsersFilter filters = new UsersFilter();
        filters.setPhoneNumber(userEntity.getPhoneNumber());
        filters.setFirstName(userEntity.getFirstName());
        filters.setLastName(userEntity.getLastName());
        filters.setCity(userEntity.getCity());
        filters.setStatuses(userEntity.getStatuses());
        return filters;
    }

}
