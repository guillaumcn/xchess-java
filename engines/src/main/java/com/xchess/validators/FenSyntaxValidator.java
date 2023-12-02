package com.xchess.validators;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validator for fen syntax
 */
public class FenSyntaxValidator {
    private FenSyntaxValidator() {
    }

    /**
     * @param fen The fen string to test
     * @return true if the fen syntax is valid
     */
    public static boolean isFenSyntaxValid(String fen) {
        Pattern regexPattern = Pattern.compile("^\\s*(((?:[rnbqkpRNBQKP1-8" +
                "]+/){7})[rnbqkpRNBQKP1-8]+)\\s([b|w])\\s(-|[KQkq]{1,4})" +
                "\\s(-|[a-h][1-8])\\s(\\d+\\s\\d+)\\s*$");

        Matcher regexMatcher = regexPattern.matcher(fen);
        if (!regexMatcher.matches()) {
            return false;
        }

        String fenBoardPart = regexMatcher.group(1);
        String[] splittedFenBoard = fenBoardPart.split("/");

        for (String fenPart :
                splittedFenBoard) {
            if (!isFenPartValid(fenPart)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param fenPart The fen part to validate
     * @return true if valid
     */
    private static boolean isFenPartValid(String fenPart) {
        boolean previousWasDigit = false;
        String[] fenPartChars = fenPart.split("");
        for (String fenPartChar :
                fenPartChars) {
            if (Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8").contains(fenPartChar)) {
                if (previousWasDigit) {
                    return false;
                }
                previousWasDigit = true;
            } else {
                previousWasDigit = false;
            }
        }
        return true;
    }
}
