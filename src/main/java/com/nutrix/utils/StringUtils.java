package com.nutrix.utils;

public class StringUtils {

    /**
     * Remove the unicode character in String
     *
     * @param value String
     * @return String
     */
    public static String removeUnicodeCharacterFromString(String value) {
        return value.replaceAll("\\p{C}", "").trim();
    }
}
