package io.dataplatform.common.util;

public class StringUtil {
    private StringUtil(){}

    public static boolean isEmpty(String s){
        if (s == null) {
            return true;
        }
        return s.isEmpty();
    }

    public static boolean isNotEmpty(String s){
        if (s == null) {
            return false;
        }
        return !s.isEmpty();
    }

}
