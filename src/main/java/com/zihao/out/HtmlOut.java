package com.zihao.out;


import com.zihao.bean.MethodCount;
import com.zihao.bean.RootScanResult;
import com.zihao.bean.SourceCount;
import com.zihao.enmu.ReplaceCountData;
import com.zihao.util.JsonUtil;
import com.zihao.util.ResourceFile;

import java.io.*;
import java.util.*;

/**
 * @author zi hao
 * @version 1.0
 * @date 2020/8/29 15:30
 * <p>
 * 数据化图形的方式展示,选择网页进行数据的展示输出
 */
public class HtmlOut extends OutCountResult {

    /* 项目代码的总行数 */
    private int lineNumber = 0;
    /* 源文件创建的最大数量 */
    private int sourceCreateNum = 0;

    /* 项目数据的简要信息展示 */
    private final List<Integer> languageCount = new ArrayList<>();

    /* 项目数据的代码信息展示 */
    private final List<Integer> methodCount = new ArrayList<>();

    /**
     * 存储根据日期时间分组的数据集
     * key : 做为不同项目的区分,虽然现在结果集类型只针对一种项目扫描
     *       不过后期进行多项目/多语言扫描的时候这里不用修改,只需要修改前面的类型就可以了
     * value: Json数据 map类型
     */
    private final Map<String,String> sourceCreateNumber = new HashMap<>();

    /* 操作资源文件的 object */
    private final ResourceFile resourceFile = new ResourceFile();

    /**
     * 涉及到多文件输出,所以不用这里单文件输出的流
     */
    public HtmlOut() {

        super(null);
        initData();
    }

    /**
     * 初始化数据,方便写出
     */
    private void initData() {
        /* 简要数据统计 */
        chooseLanguageCount();
        /* 项目函数统计 */
        chooseMethodCount();
        /* 日期源码统计 */
        chooseSourceFileCreatedNumber();
    }

    /**
     * 简要信息统计,选出所需要的数据
     * 源文件个数,if,for,while
     */
    public void chooseLanguageCount() {

        /* 遍历当前语言统计结果 */
        for (Map.Entry<String, Object> nowLanguageCount : RootScanResult.getCountResult().entrySet()) {

            SourceCount sourceCount = (SourceCount) nowLanguageCount.getValue();
            /* 项目代码的总行数 */
            lineNumber = sourceCount.getLineAllNumber();
            /* 取出简要信息的数据 */
            languageCount.add(sourceCount.getSourceFileNumber());
            languageCount.add(sourceCount.getIfNumber());
            languageCount.add(sourceCount.getForNumber());
            languageCount.add(sourceCount.getWhileNumber());
            /* 取出代码信息的数据 */
            methodCount.add(sourceCount.getMethodNumber());
        }
    }

    /**
     * 代码信息统计,选出所需要的数据
     * 函数个数,最长的方法行,平均方法行
     */
    public void chooseMethodCount() {

        /* 存储最长函数代码行数 */
        int maxLengthMethod = 0;
        /* 存储函数所有代码行数 */
        int allLengthMethod = 0;
        /* 存储函数代码平均行数 */
        int avgLengthMethod = 0;

        for (Map.Entry<String, List<MethodCount>> nowMethodCount
                : RootScanResult.getCountMethodResult().entrySet()) {

            List<MethodCount> methodCounts = nowMethodCount.getValue();
            for (MethodCount nowCount : methodCounts) {

                if (maxLengthMethod < nowCount.getLineAllNumber())
                    maxLengthMethod = nowCount.getLineAllNumber();

                allLengthMethod += nowCount.getLineAllNumber();
            }
        }

        avgLengthMethod = allLengthMethod / methodCount.get(0);

        methodCount.add(maxLengthMethod);
        methodCount.add(avgLengthMethod);
    }

    /**
     * 源码文件分类根据日期统计当天所create的源码文件个数
     * @param sourceFile 扫描的结果集
     * @return 按日期排好序的日期格式
     */
    private List<String> sourceFileCreatedNumberInit(Map<String,List<File>> sourceFile) {

        if (null == sourceFile || sourceFile.isEmpty()) {
            return null;
        }

        List<String> keys = new ArrayList<>();
        /* 取出keys */
        for (Map.Entry<String,List<File>> nowScanDate : sourceFile.entrySet()) {

            keys.add(nowScanDate.getKey());
        }

        /* 将数据进行排序 */
        Collections.sort(keys, new Comparator<String>() {

            @Override
            public int compare(String s, String t1) {

                return s.compareTo(t1);
            }
        });

        return keys;
    }

    /**
     * 筛选数据,取出当天日期下所创建的源代码文件
     */
    public void chooseSourceFileCreatedNumber() {

        Map<String,List<File>> sourceFile = RootScanResult.getCountGroupByDate();
        List<String> dates = sourceFileCreatedNumberInit(sourceFile);

        /* 进行数据格式的整合 */
        String groupCreateResult = "[";
        for (String nowDate : dates) {

            int sourceFileSize = sourceFile.get(nowDate).size();
            sourceCreateNum = (sourceCreateNum < sourceFileSize) ? sourceFileSize : sourceCreateNum;

            groupCreateResult += JsonUtil.getJsonMapType(nowDate,sourceFileSize);
            if (nowDate.equals(dates.get(dates.size() - 1))) {
                groupCreateResult += "]";
            } else {
                groupCreateResult += ",";
            }
        }

        /* 存储整理好的数据 */
        File tempFile = sourceFile.get(dates.get(0)).get(0);
        String suffixName = resourceFile.getFileSuffixName(tempFile);
        sourceCreateNumber.put(suffixName,groupCreateResult);
    }

    @Override
    public void print() {

        try {
            outCssFile();
            outEchartsFile();
            outCodeViewFile();
            outCountHtmlFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出CSS样式文件
     * 后期可能优化,更换其他的输出方式
     */
    private void outCssFile() {

        /* 资源数据文件写出 */
        resourceFile.resourceReadAndWrite("static/style.css", "count/style.css");
    }

    /**
     * 输出Echarts文件
     * 后期可能优化,更换其他的输出方式
     */
    private void outEchartsFile() {

        /* 资源数据文件写出 */
        resourceFile.resourceReadAndWrite("static/echarts.min.js", "count/echarts.min.js");
    }

    /**
     * 输出html网页文件
     * 后期可能优化,更换其他的输出方式
     */
    private void outCountHtmlFile() {

        /* 替换html缓存的数据 并将资源文件写出 */
        resourceFile.replaceHtmlData(ReplaceCountData.CODE_LINE_NUMBER.getReplaceName(),lineNumber);
        try {
            resourceFile.writeResourceCaseFile(resourceFile.getHtmlBuf(),"count/count.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 输出数据统计显示js文件
     * 后期可能优化,更换其他的输出方式
     * 该函数会进行动态数据替换,不在调用公共方法
     */
    private void outCodeViewFile() {

        /* 替换简要信息数据 */
        resourceFile.replaceCodeViewData(ReplaceCountData.LANGUAGE_COUNT.getReplaceName(),languageCount);
        /* 替换methodCount数据 */
        resourceFile.replaceCodeViewData(ReplaceCountData.METHOD_COUNT.getReplaceName(),methodCount);
        /* 替换sourceCreate数据 */
        resourceFile.replaceCodeViewData(ReplaceCountData.SOURCE_CREATE_NUM.getReplaceName(),sourceCreateNumber.get("java"));

        try {
            resourceFile.writeResourceCaseFile(resourceFile.getCodeViewBuf(),"count/code-view.js");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
