package com.xchess.validators;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FenSyntaxValidator {
    public static boolean isFenSyntaxValid(String fen) {
        Pattern regexPattern = Pattern.compile("\\s*^(((?:[rnbqkpRNBQKP1-8" +
                "]+\\/){7})[rnbqkpRNBQKP1-8]+)\\s([b|w])\\s(-|[K|Q|k|q]{1,4})" +
                "\\s(-|[a-h][1-8])\\s(\\d+\\s\\d+)$");

        Matcher regexMatcher = regexPattern.matcher(fen);
        if (!regexMatcher.matches()) {
            return false;
        }

        String entireFen = regexMatcher.group(0);
        String[] splittedFen = entireFen.split("/");

        if (splittedFen.length != 8) {
            return false;
        }

        for (String fenPart :
                splittedFen) {
            int fieldSum = 0;
            boolean previousWasDigit = false;
            String[] fenPartChars = fenPart.split("");
            for (String fenPartChar :
                    fenPartChars) {
                if (Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8").contains(fenPartChar)) {
                    if (previousWasDigit) {
                        return false;
                    }
                    fieldSum += Integer.parseInt(fenPartChar);
                    previousWasDigit = true;
                } else if (PieceValidator.isPieceValid(fenPartChar)) {
                    fieldSum += 1;
                    previousWasDigit = false;
                } else {
                    return false;
                }
            }
            if (fieldSum == 0) {
                return false;
            }
        }
        return true;
    }
}
