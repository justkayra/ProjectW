package com.semantyca.projectw.util;


import java.util.Random;

public class NumberUtil {

    public static int getRandomNumber(int low, int high) {
        if (low == high) {
            return low;
        } else {
            Random r = new Random();
            int result = r.nextInt(high - low) + low;
            return result;
        }
    }

    public static int countMaxPage(long colCount, int pageSize) {
        float mp = (float) colCount / (float) pageSize;
        float d = Math.round(mp);

        int maxPage = (int) d;
        if (mp > d) {
            maxPage++;
        }
        if (maxPage < 1) {
            maxPage = 1;
        }
        return maxPage;
    }

    public static int calcStartEntry(int pageNum, int pageSize) {
        int pageNumMinusOne = pageNum;
        pageNumMinusOne--;
        if (pageNumMinusOne < 0) {
            return 0;
        }
        return pageNumMinusOne * pageSize;
        //return pageNum * pageSize;
    }

    public static int stringToInt(String text, int defaultValue) {
        try {
            return stringToInt(text);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int stringToInt(String text) {
        try {
            text = text.replaceAll("\\s+", "").replaceAll(",", "").replaceAll("/\\D/g", "").replaceAll("\\p{IsCyrillic}", "");
            return Integer.parseInt(text);
        } catch (Exception e) {
            throw e;
        }
    }

}
