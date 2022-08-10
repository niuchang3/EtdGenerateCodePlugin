package org.etd.generate.code.plugin.utils;

/**
 * 字符串工具类
 *
 * @author 牛昌
 */
public class StringUtil extends ToHumpUtil {

    private volatile static StringUtil stringUtil;

    private StringUtil() {

    }

    public static StringUtil getSingleton() {
        if (stringUtil == null) {
            synchronized (StringUtil.class) {
                if (stringUtil == null) {
                    stringUtil = new StringUtil();
                }
            }
        }
        return stringUtil;
    }

    public String append(String... strs) {

        if (strs == null || strs.length == 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (String s : strs) {
            if (s != null) {
                builder.append(s);
            }
        }
        return builder.toString();
    }




}
