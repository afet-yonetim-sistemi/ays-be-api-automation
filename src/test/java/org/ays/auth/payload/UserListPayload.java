package org.ays.auth.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.auth.model.entity.UserEntity;
import org.ays.auth.model.enums.UserStatus;
import org.ays.common.model.payload.AysPageable;
import org.ays.common.model.payload.AysPhoneNumber;

import java.util.List;

@Getter
@Setter
public class UserListPayload {

    private AysPageable pageable;
    private Filter filter;

    public static UserListPayload generate(AysPhoneNumber phoneNumber) {
        UserListPayload userListPayload = new UserListPayload();
        userListPayload.setPageable(AysPageable.generateFirstPage());
        Filter filters = new Filter();
        filters.setPhoneNumber(phoneNumber);
        userListPayload.setFilter(filters);
        return userListPayload;
    }

    @Getter
    @Setter
    public static class Filter {

        private String firstName;
        private String lastName;
        private String emailAddress;
        private String city;
        private List<String> statuses;
        private AysPhoneNumber phoneNumber;

        public static Filter from(UserEntity userEntity, List<UserStatus> userStatuses) {
            Filter filters = new Filter();
            filters.setPhoneNumber(userEntity.getPhoneNumber());
            filters.setFirstName(userEntity.getFirstName());
            filters.setLastName(userEntity.getLastName());
            filters.setEmailAddress(userEntity.getEmailAddress());
            filters.setCity(userEntity.getCity());
            filters.setStatuses(userStatuses.stream()
                    .map(Enum::name)
                    .toList());
            return filters;
        }

    }


}
