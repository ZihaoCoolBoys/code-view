package com.zihao.out;


import com.zihao.bean.MethodCount;
import com.zihao.bean.RootScanResult;
import com.zihao.bean.SourceCount;
import com.zihao.enmu.ReplaceCountData;
import com.zihao.util.JsonUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /* 项目数据的简要信息展示 */
    private List<Integer> languageCount = new ArrayList<>();

    /* 项目数据的代码信息展示 */
    private List<Integer> methodCount = new ArrayList<>();

    /* 资源文件根路径 */
    private final String rootPath
            = this.getClass().getClassLoader().getResource("").getPath();

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
     * 统一关闭资源
     * 文件输入/输出流
     * 以下函数都会对应一个打开以及关闭的文件流
     *
     * @param in
     * @param out
     */
    public void closeResource(FileInputStream in, FileOutputStream out) {

        try {

            if (null != in)
                in.close();

            if (null != out)
                out.close();

        } catch (IOException error) {

            error.printStackTrace();
        }
    }

    /**
     * 统一资源写入/写出
     *
     * @param readPath
     * @param writePath
     */
    private void resourceReadAndWrite(String readPath, String writePath) {

        /* 打开需要载入的资源文件 */
        File resourcePath = new File(this.rootPath + readPath);

        /* 写入/写出 */
        FileInputStream loadResource = null;
        FileOutputStream writeResource = null;

        /* 文件不存在不写出 & 根目录不存在不写出 */
        if (createOutResultDir() && !resourcePath.exists()) {
            return;
        }

        try {

            loadResource = new FileInputStream(resourcePath);
            writeResource = new FileOutputStream(new File(writePath));

            int len = -1;
            byte[] buf = new byte[1024];
            while (-1 != (len = loadResource.read(buf))) {
                writeResource.write(buf, 0, len);
            }

        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            /* 资源释放 */
            closeResource(loadResource, writeResource);
        }
    }

    /**
     * 创建结果集输出目录
     */
    private boolean createOutResultDir() {

        File file = new File("count");

        if (!file.exists()) {
            file.mkdirs();
            return true;
        }

        return false;
    }

    /**
     * 输出CSS样式文件
     * 后期可能优化,更换其他的输出方式
     */
    private void outCssFile() {

        /* 资源数据文件写出 */
        resourceReadAndWrite("static/style.css", "count/style.css");
    }

    /**
     * 输出Echarts文件
     * 后期可能优化,更换其他的输出方式
     */
    private void outEchartsFile() {

        /* 资源数据文件写出 */
        resourceReadAndWrite("static/echarts.min.js", "count/echarts.min.js");
    }

    /**
     * 输出html网页文件
     * 后期可能优化,更换其他的输出方式
     */
    private void outCountHtmlFile() {

        /* 资源数据文件写出 */
        //resourceReadAndWrite( "static/count.html", "count/count.html");

        /* 资源数据文件写出 并动态更换总代码行数 */
        try {

            /* 读为文本 */
            StringBuilder builder = loadResourceCaseStr(this.rootPath + "static/count.html");
            replaceCodeLineNumber(builder);
            writeResourceCaseFile(builder,"count/count.html");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换网页部分总共代码的行数
     * @param builder
     */
    private void replaceCodeLineNumber(StringBuilder builder) {

        /* 寻找需要替换的位置 */
        int replaceIndex = -1;
        /* 替换codeLineNumber数据 */
        replaceIndex = builder.lastIndexOf(ReplaceCountData.CODE_LINE_NUMBER.getReplaceName());
        if (-1 != replaceIndex) {

            builder.replace(replaceIndex
                    ,replaceIndex + ReplaceCountData.CODE_LINE_NUMBER.getReplaceName().length()
                    ,String.valueOf(lineNumber));
        }
    }

    /**
     * 数据动态替换,替换简单数据,替换
     */
    private void replaceCodeViewData(StringBuilder builder) {

        /* 寻找需要替换的位置 */
        int replaceIndex = -1;
        /* 替换languageCount数据 */
        replaceIndex = builder.lastIndexOf(ReplaceCountData.LANGUAGE_COUNT.getReplaceName());
        if (-1 != replaceIndex) {

            builder.replace(replaceIndex
                    , replaceIndex + ReplaceCountData.LANGUAGE_COUNT.getReplaceName().length()
                    , JsonUtil.getJson(languageCount));

            replaceIndex = -1;
        }
        /* 替换methodCount数据 */
        replaceIndex = builder.lastIndexOf(ReplaceCountData.METHOD_COUNT.getReplaceName());
        if (-1 != replaceIndex) {

            builder.replace(replaceIndex
                    , replaceIndex + ReplaceCountData.METHOD_COUNT.getReplaceName().length()
                    , JsonUtil.getJson(methodCount));
        }
    }

    /**
     * 将需要动态替换的数据加载进来
     * 读入文本进行传出
     */
    private StringBuilder loadResourceCaseStr(String path) throws Exception {

        File codeViewFile = new File(path);

        /* 将需要动态更换数据的资源文件读取 */
        StringBuilder builder = new StringBuilder();

        /* 文件不存在不写出 & 根目录不存在不写出 */
        if (!codeViewFile.exists() && createOutResultDir()) {
            return null;
        }

        /* 将该js文件全部加载进来 */
        Reader codeViewReader = null;
        BufferedReader codeViewBuf = null;

        try {

            codeViewReader = new FileReader(codeViewFile);
            codeViewBuf = new BufferedReader(codeViewReader);

            String line = null;
            while (null != (line = codeViewBuf.readLine())) {
                builder.append(line);
            }

            return builder;
        } finally {

            if (null != codeViewBuf) {
                codeViewBuf.close();
            }
        }
    }

    /**
     * 将替换好的资源文件写出
     *
     * @param builder
     */
    private void writeResourceCaseFile(StringBuilder builder,String path) throws Exception {

        if (null == builder) {
            throw new NullPointerException();
        }

        File codeViewPath = new File(path);

        if (createOutResultDir()) {
            return;
        }

        FileWriter writer = null;
        BufferedWriter writerBuf = null;

        try {

            writer = new FileWriter(codeViewPath);
            writerBuf = new BufferedWriter(writer);
            writerBuf.write(builder.toString());
        } finally {

            if (null != writerBuf) {
                writerBuf.close();
            }
        }
    }

    /**
     * 输出数据统计显示js文件
     * 后期可能优化,更换其他的输出方式
     * 该函数会进行动态数据替换,不在调用公共方法
     */
    private void outCodeViewFile() throws Exception {

        try {

            /* 数据动态替换 */
            StringBuilder builder = loadResourceCaseStr(this.rootPath + "static/code-view.js");
            replaceCodeViewData(builder);
            writeResourceCaseFile(builder,"count/code-view.js");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
