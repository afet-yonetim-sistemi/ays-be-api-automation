package org.ays.auth.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.payload.AysPhoneNumber;
import org.ays.common.util.AysRandomUtil;

import java.util.List;

@Getter
@Setter
public class UserCreatePayload {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private AysPhoneNumber phoneNumber;
    private String city;
    private List<String> roleIds;

    public static UserCreatePayload generate() {
        UserCreatePayload userCreatePayload = new UserCreatePayload();
        userCreatePayload.setFirstName(AysRandomUtil.generateFirstName());
        userCreatePayload.setLastName(AysRandomUtil.generateLastName());
        userCreatePayload.setPhoneNumber(AysPhoneNumber.generateForTurkey());
        userCreatePayload.setEmailAddress(AysRandomUtil.generateEmailAddress());
        userCreatePayload.setCity(AysRandomUtil.generateRandomCity());
        userCreatePayload.setRoleIds(List.of("42fe288d-be87-4169-875e-e721a75cc833"));
        return userCreatePayload;
    }

    public static UserCreatePayload generateUserWithARole(String roleId) {
        UserCreatePayload userCreatePayload = new UserCreatePayload();
        userCreatePayload.setFirstName(AysRandomUtil.generateFirstName());
        userCreatePayload.setLastName(AysRandomUtil.generateLastName());
        userCreatePayload.setPhoneNumber(AysPhoneNumber.generateForTurkey());
        userCreatePayload.setEmailAddress(AysRandomUtil.generateEmailAddress());
        userCreatePayload.setCity(AysRandomUtil.generateRandomCity());
        userCreatePayload.setRoleIds(List.of(roleId));
        return userCreatePayload;
    }

}
