package org.ays.common.util;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class AysRandomUtil {

    private static final Faker FAKER = new Faker();
    private static final Random RANDOM = new Random();

    public static String generateFirstName() {
        return FAKER.name().firstName();
    }

    public static String generateLastName() {
        return FAKER.name().lastName();
    }

    public String generateEmailAddress() {
        final String firstName = FAKER.name().firstName().toLowerCase().replaceAll("[^a-z]", "");
        final String lastName = FAKER.name().lastName().toLowerCase().replaceAll("[^a-z]", "");
        final String prefix = "test";
        return (prefix + firstName + "." + lastName + "@afetyonetimsistemi.test");
    }

    public static String generatePassword() {
        return FAKER.internet().password();
    }

    public static String generatePassword(int minLength, int maxLength) {

        if (minLength < 1 || maxLength < minLength) {
            throw new IllegalArgumentException("Invalid password length range." +
                    " Ensure minLength >= 1 and maxLength >= minLength.");
        }

        if (minLength == maxLength) {
            return FAKER.internet().password(minLength, minLength + 1).substring(0, minLength);
        }

        return FAKER.internet().password(minLength, maxLength);
    }

    public static String generateReasonString() {
        int length = 40 + FAKER.number().numberBetween(0, 472);
        return FAKER.lorem().characters(length);
    }

    public static String generateRandomDigits(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            stringBuilder.append(RANDOM.nextInt(10));
        }
        return stringBuilder.toString();
    }

    public static String generateLineNumber() {
        StringBuilder phoneNumberBuilder = new StringBuilder();
        int[] prefixesArray = {212, 216, 222, 224, 226, 228, 232, 236, 242, 246, 248, 252, 256, 258, 262, 264, 266, 272, 274, 276, 282, 284, 286, 288, 312, 318, 322, 324, 326, 328, 332, 338, 342, 344, 346, 348, 352, 354, 356, 358, 362, 364, 366, 368, 370, 372, 374, 376, 378, 380, 382, 384, 386, 388, 392, 412, 414, 416, 422, 424, 426, 428, 432, 434, 436, 438, 442, 446, 452, 454, 456, 458, 462, 464, 466, 472, 474, 476, 478, 482, 484, 486, 488, 501, 505, 506, 507, 551, 552, 553, 554, 555, 559, 516, 530, 531, 532, 533, 534, 535, 536, 537, 538, 539, 540, 541, 542, 543, 544, 545, 546, 547, 548, 549};
        int randomIndex = RANDOM.nextInt(prefixesArray.length);
        int selectedPrefix = prefixesArray[randomIndex];
        phoneNumberBuilder.append(selectedPrefix);
        for (int i = 0; i < 7; i++) {
            int digit = RANDOM.nextInt(10);
            phoneNumberBuilder.append(digit);
        }
        return phoneNumberBuilder.toString();
    }

    public static String generateInvalidLineNumber() {
        int[] prefixesArray = {212, 216, 222, 224, 226, 228, 232, 236, 242, 246, 248, 252, 256, 258, 262, 264, 266, 272, 274, 276, 282, 284, 286, 288, 312, 318, 322, 324, 326, 328, 332, 338, 342, 344, 346, 348, 352, 354, 356, 358, 362, 364, 366, 368, 370, 372, 374, 376, 378, 380, 382, 384, 386, 388, 392, 412, 414, 416, 422, 424, 426, 428, 432, 434, 436, 438, 442, 446, 452, 454, 456, 458, 462, 464, 466, 472, 474, 476, 478, 482, 484, 486, 488, 501, 505, 506, 507, 551, 552, 553, 554, 555, 559, 516, 530, 531, 532, 533, 534, 535, 536, 537, 538, 539, 540, 541, 542, 543, 544, 545, 546, 547, 548, 549};

        String phoneNumber;
        do {
            int firstThreeDigits = RANDOM.nextInt(900) + 100;
            phoneNumber = firstThreeDigits + generateRandomDigits(7);
        } while (isPrefixInArray(Integer.parseInt(phoneNumber.substring(0, 3)), prefixesArray));
        return phoneNumber;
    }

    public static boolean isPrefixInArray(int prefix, int[] prefixesArray) {
        for (int i : prefixesArray) {
            if (i == prefix) {
                return true;
            }
        }
        return false;
    }

    public static String generateRejectionReason() {
        return FAKER.lorem().characters(50, 100);
    }

    public static String generateRandomCity() {
        return FAKER.address().city();
    }

    public static String generateRandomDistrict() {
        return FAKER.address().state();
    }

    public static String generateRandomAddress() {
        return FAKER.address().fullAddress();
    }

    public static int generateSeatingCount() {
        return RANDOM.nextInt(999) + 1;
    }

    public static String generateAlphaSuffix() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder suffix = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            suffix.append(alphabet.charAt(RANDOM.nextInt(alphabet.length())));
        }

        return suffix.toString();
    }

}
