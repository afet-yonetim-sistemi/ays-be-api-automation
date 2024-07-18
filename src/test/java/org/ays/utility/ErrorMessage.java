package org.ays.utility;

public enum ErrorMessage {
    MUST_NOT_BE_BLANK("must not be blank"),
    MUST_BE_VALID("must be valid"),
    SIZE_BETWEEN("size must be between 20 and 250"),
    SEATING_COUNT_BETWEEN("must be between 1 and 999"),
    ALL_APPLICANT_FIELDS_FILLED("all applicant fields must be filled"),
    SIZE_BETWEEN_2_100("size must be between 2 and 100"),
    SIZE_BETWEEN_1_10("size must be between 1 and 10"),
    CONTAINS_INVALID_CHARACTERS("contains invalid characters"),
    MUST_BE_ACCEPTED_VALUE("must be accepted value"),
    MUST_NOT_BE_NULL("must not be null"),
    MUST_BE_TRUE("must be true"),
    NAME_MUST_NOT_START_OR_END_WITH_WHITESPACE("name must not start or end with whitespace"),
    SIZE_MUST_BE_BETWEEN_2_AND_255("size must be between 2 and 255");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
