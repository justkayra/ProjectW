package com.semantyca.util;

import java.util.Random;

public class StringUtil {

    public static String cleanFromMarkdown(String text) {
        String resultText = text.replace("*", "").replace("**", "").replaceAll("`", "");
        return resultText;
    }

    public static String getRndText() {
        return getRndText(10 ,"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890");
    }

    public static String getRndText(int len, String chars) {
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < len) {
            int index = (int) (rnd.nextFloat() * chars.length());
            salt.append(chars.charAt(index));
        }
        return salt.toString();
    }

    public static String getRndColor() {
        Random rand = new Random();
        int myRandomNumber = rand.nextInt(0x10) + 0x10;
        return Integer.toHexString(myRandomNumber);
    }


}
