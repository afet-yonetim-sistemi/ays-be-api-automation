package org.ays.auth.payload;

import lombok.Getter;
import lombok.Setter;
import org.ays.common.model.payload.AysPhoneNumber;
import org.ays.common.util.AysRandomUtil;

import java.util.List;

@Getter
@Setter
public class UserUpdatePayload {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private AysPhoneNumber phoneNumber;
    private String city;
    private List<String> roleIds;

    public static UserUpdatePayload generate() {
        UserUpdatePayload userUpdatePayload = new UserUpdatePayload();
        userUpdatePayload.setFirstName(AysRandomUtil.generateFirstName());
        userUpdatePayload.setLastName(AysRandomUtil.generateLastName());
        userUpdatePayload.setPhoneNumber(AysPhoneNumber.generateForTurkey());
        userUpdatePayload.setEmailAddress(AysRandomUtil.generateEmailAddress());
        userUpdatePayload.setCity(AysRandomUtil.generateRandomCity());
        userUpdatePayload.setRoleIds(List.of("42fe288d-be87-4169-875e-e721a75cc833"));
        return userUpdatePayload;
    }

    public static UserUpdatePayload generateUserWithARole(String roleId) {
        UserUpdatePayload userUpdatePayload = new UserUpdatePayload();
        userUpdatePayload.setFirstName(AysRandomUtil.generateFirstName());
        userUpdatePayload.setLastName(AysRandomUtil.generateLastName());
        userUpdatePayload.setPhoneNumber(AysPhoneNumber.generateForTurkey());
        userUpdatePayload.setEmailAddress(AysRandomUtil.generateEmailAddress());
        userUpdatePayload.setCity(AysRandomUtil.generateRandomCity());
        userUpdatePayload.setRoleIds(List.of(roleId));
        return userUpdatePayload;
    }

    public static UserUpdatePayload from(UserCreatePayload userCreatePayload) {
        UserUpdatePayload userUpdatePayload = new UserUpdatePayload();
        userUpdatePayload.setFirstName(userCreatePayload.getFirstName());
        userUpdatePayload.setLastName(userCreatePayload.getLastName());
        userUpdatePayload.setPhoneNumber(userCreatePayload.getPhoneNumber());
        userUpdatePayload.setEmailAddress(userCreatePayload.getEmailAddress());
        userUpdatePayload.setCity(userCreatePayload.getCity());
        userUpdatePayload.setRoleIds(userCreatePayload.getRoleIds());
        return userUpdatePayload;
    }

}
