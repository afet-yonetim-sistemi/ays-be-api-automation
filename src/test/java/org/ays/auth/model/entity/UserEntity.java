package org.ays.auth.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.payload.AysPhoneNumber;
import java.util.List;

@Getter
@Setter
public class UserEntity {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String city;
    private List<String> statuses;
    private AysPhoneNumber phoneNumber;

}
