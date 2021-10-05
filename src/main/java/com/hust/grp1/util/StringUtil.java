package com.hust.grp1.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String getSqlSearchFormat(String searchKey) {
        String sqlSearchFormat = "%x%";
        sqlSearchFormat = sqlSearchFormat.replaceAll("x", searchKey.replaceAll(" ", "%"));
        return sqlSearchFormat;
    }

    public static boolean validatePassword(String rawPassword) {
        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
        Matcher matcher =passwordPattern.matcher(rawPassword);
        if(matcher.matches()) return true;
        return false;
    }
}
