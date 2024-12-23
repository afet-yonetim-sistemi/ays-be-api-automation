package org.ays.common.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AysErrorMessage {

    MUST_NOT_BE_BLANK("must not be blank"),
    MUST_BE_VALID("must be valid"),
    SIZE_BETWEEN("size must be between 20 and 250"),
    SEATING_COUNT_BETWEEN("must be between 1 and 999"),
    ALL_APPLICANT_FIELDS_FILLED("all applicant fields must be filled"),
    SIZE_BETWEEN_2_100("size must be between 2 and 100"),
    SIZE_BETWEEN_2_255("size must be between 2 and 255"),
    SIZE_BETWEEN_1_10("size must be between 1 and 10"),
    SIZE_BETWEEN_40_512("size must be between 40 and 512"),
    CONTAINS_INVALID_CHARACTERS("contains invalid characters"),
    MUST_BE_ACCEPTED_VALUE("must be accepted value"),
    MUST_NOT_BE_NULL("must not be null"),
    MUST_BE_TRUE("must be true"),
    NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE("name must not start or end with whitespace"),
    CANNOT_START_OR_END_WITH_SPACE("cannot start or end with space"),
    MUST_BE_VALID_UUID("must be a valid UUID"),
    MUST_NOT_BE_EMPTY("must not be empty"),
    MUST_BE_BETWEEN_1_AND_99999999("must be between 1 and 99999999"),
    ROLE_STATUS_IS_NOT_ACTIVE("role status is not active!"),
    ROLE_DOES_NOT_EXIST("role does not exist!"),
    ROLE_STATUS_IS_NOT_PASSIVE("role status is not passive!"),
    THE_ROLE_IS_ASSIGNED("the role is assigned to one or more users"),
    MUST_BE_10("must be 10"),
    ADMIN_REGISTRATION_APPLICATION_ALREADY_APPROVED_OR_REJECTED("admin registration application was already approved or rejected!"),
    ADMIN_REGISTRATION_APPLICATION_ALREADY_REJECTED("admin registration application was already rejected!"),
    ADMIN_REGISTRATION_APPLICATION_IS_NOT_COMPLETE("admin registration application is not complete!"),
    EMAIL_MUST_NOT_START_OR_END_WITH_WHITESPACE("email must not start or end with whitespace"),
    EMAIL_CONTAIN_EXACTLY_ONE_AT_CHARACTER("email must contain exactly one '@' character"),
    EMAIL_CONTAINS_INVALID_SPECIAL_CHARACTERS("email contains invalid special characters"),
    EMAIL_LOCAL_PART_MUST_START_WITH_A_LETTER_OR_NUMBER("email local part must start with a letter or number"),
    DOMAIN_MUST_NOT_START_OR_END_WITH_A_HYPHEN("domain must not start or end with a hyphen"),
    EMAIL_IS_NOT_IN_A_VALID_FORMAT("email is not in a valid format");


    private final String message;

}
