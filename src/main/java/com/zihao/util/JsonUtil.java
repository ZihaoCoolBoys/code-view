package com.zihao.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

/**
 * @author zi hao
 * @version 1.0
 * @date 2020/8/30 16:46
 *
 * JSON转换工具类
 *
 */
public class JsonUtil {

    private static final ObjectMapper mapper
            = new ObjectMapper();

    /**
     * 将object序列化为json格式数据
     * @return
     */
    public static String getJson(Object bean) {

        if (null == bean) {
            return null;
        }

        try {

            String jsonData = mapper.writeValueAsString(bean);
            return jsonData;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     *
     * @param date
     * @param sourceFileNumber
     * @return
     */
    public static String getJsonMapType(String date, int sourceFileNumber) {

        if (null == date || sourceFileNumber <= 0) {
            return null;
        }

        String mapFormat = "[\"" + date + "\"," + sourceFileNumber + "]";
        return mapFormat;
    }
}
