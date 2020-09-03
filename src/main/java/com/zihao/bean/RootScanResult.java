package com.zihao.bean;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 结果集的汇总
 */

public class RootScanResult {

    /**
     * 存储每种语言的扫描结果
     * key ->  语言类型,比如 java,就是源码文件的后缀名
     * value -> SourceCount
     * if语句个数,for语句个数,while语句个数,方法的数量,源码文件个数,总行数
     */
    private final static Map<String,Object> countResult = new HashMap<>();

    /**
     * 存储每种方法的描述结果
     * key -> 源码文件的绝对路径
     * value -> 每个方法的具体,方法名称,方法行数
     * 如果想更具体的分析,可能也会存储该方法的具体内容
     */
    private final static Map<String, List<MethodCount>> countMethodResult = new HashMap<>();

    /**
     * 日期统计分组
     * 将当前创建的源码文件进行汇总
     * key 做为当天日期
     * value 做为当前日期所有的源代码文件
     *
     * 后期:目前只针对java语言所定义的单语言统计所创建的数据类型
     * 后期要支持多项目,多语言统计
     */
    private final static Map<String,List<File>> countGroupByDate = new HashMap<>();

    public static Map<String,Object> getCountResult() {
        return countResult;
    }

    public static Map<String, List<MethodCount>> getCountMethodResult() {
        return countMethodResult;
    }

    public static Map<String,List<File>> getCountGroupByDate() {
        return countGroupByDate;
    }
}
