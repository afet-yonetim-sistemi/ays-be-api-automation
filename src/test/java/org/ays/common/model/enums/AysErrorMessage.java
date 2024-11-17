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
    MUST_BE_VALID_UUID("must be a valid UUID"),
    MUST_NOT_BE_EMPTY("must not be empty"),
    MUST_BE_BETWEEN_1_AND_99999999("must be between 1 and 99999999"),
    ROLE_STATUS_IS_NOT_ACTIVE("role status is not active!"),
    ROLE_DOES_NOT_EXIST("role does not exist!"),
    ROLE_STATUS_IS_NOT_PASSIVE("role status is not passive!"),
    THE_ROLE_IS_ASSIGNED("the role is assigned to one or more users"),
    MUST_BE_10("must be 10"),
    STATUS_APPROVED("status:APPROVED"),
    STATUS_REJECTED("already rejected!"),
    STATUS_NOT_COMPLETED("is not complete!");

    private final String message;

}
