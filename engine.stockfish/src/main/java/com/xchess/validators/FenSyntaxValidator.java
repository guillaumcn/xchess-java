package com.xchess.validators;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FenSyntaxValidator {
    private FenSyntaxValidator() {
    }

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

        if (splittedFenBoard.length != 8) {
            return false;
        }

        for (String fenPart :
                splittedFenBoard) {
            if (!isFenPartValid(fenPart)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isFenPartValid(String fenPart) {
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
        return fieldSum != 0;
    }
}
