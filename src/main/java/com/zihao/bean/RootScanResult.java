package com.zihao.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 结果集的汇总
 */

public class RootScanResult {

    //存储每种语言的扫描结果
    private final static Map<String,Object> countResult =
            new HashMap<>();

    //存储每种方法的描述结果
    private final static Map<String, List<MethodCount>> countMethodResult =
            new HashMap<>();

    public static Map<String,Object> getCountResult() {
        return countResult;
    }

    public static Map<String, List<MethodCount>> getCountMethodResult() {
        return countMethodResult;
    }
}
