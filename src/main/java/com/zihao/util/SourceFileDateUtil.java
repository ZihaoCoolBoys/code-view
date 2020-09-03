package com.zihao.util;

import java.text.SimpleDateFormat;

/**
 * @author zi hao
 * @version 1.0
 * @date 2020/9/2 16:39
 *
 * 源码文件日期工具类,用于格式化日期格式
 */

public class SourceFileDateUtil {

    private static final SimpleDateFormat simpleDateFormat
            = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 格式化日期格式
     * @param sourceDate 源码文件日期
     * @return
     */
    public static String formatSourceDate(long sourceDate) {

        if (sourceDate <= 0) {
            return null;
        }

        return simpleDateFormat.format(sourceDate);
    }
}
