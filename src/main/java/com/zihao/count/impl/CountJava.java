package com.zihao.count.impl;

import com.zihao.bean.RootScanResult;
import com.zihao.bean.SourceCount;
import com.zihao.bean.MethodCount;
import com.zihao.count.JavaCodeCount;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *  JAVA项目分析实例
 */

public class CountJava implements JavaCodeCount {

    private final static String COUNT_TEMPLATE_FLAG_JAVA = "java";
    private final static String[] JAVA_JURISDICTION = {"public", "private", "protected"};

    private File codeFile;

    private SourceCount javaResult;

    private String methodName = null;
    private MethodCount methodCount = null;

    private int leftJurisdictionCount = 0;
    private int rightJurisdictionCount = 0;

    public CountJava() {
    }

    @Override
    public void ifCount(String nowLine) {
        int liftPos = nowLine.indexOf("if");
        int rightPos = nowLine.lastIndexOf("(");

        if (-1 != liftPos && -1 != rightPos && liftPos < rightPos)
            javaResult.setIfNumber(javaResult.getIfNumber() + 1);
    }

    public void methodCountInit(String line) {
        List<MethodCount> methodCounts
                = RootScanResult.getCountMethodResult().get(codeFile.getAbsolutePath());
        if ( null == methodCounts) {
            methodCounts = new ArrayList<>();
            RootScanResult.getCountMethodResult().put(codeFile.getAbsolutePath(),methodCounts);
        }

        methodCount = new MethodCount();
        methodCount.setMethodName(line.substring(0,line.lastIndexOf('{')));

        methodCounts.add(methodCount);
        methodName = line; //标识现在处于方法阶段
    }

    /**
     * 源代码方法统计
     *
     * @param nowLine
     */
    @Override
    public void methodCount(String nowLine) {
        boolean hasJurisdiction = false;
        for (String jurisdiction : JAVA_JURISDICTION) {

            if (-1 != nowLine.indexOf(jurisdiction)) {
                hasJurisdiction = true;
                break;
            }
        }

        //证明当前进入了一个方法
        if (hasJurisdiction && nowLine.indexOf("(") < nowLine.indexOf(")") && -1 != nowLine.indexOf('{')) {
            methodCountInit(nowLine);
            javaResult.setMethodNumber(javaResult.getMethodNumber() + 1);
        }
    }

    /**
     * 统计 大括号的数量
     * @param line
     */
    public void countMethodRegx(String line,char data) {
        int leftCount = line.indexOf(data);
        while ( leftCount != -1) {
            //检测 " 常量问题
            int doubleLeftCount = line.indexOf("\"");
            int doubleRightCount = line.lastIndexOf("\"");

            //检测 ' 常量问题
            int singleLeftCount = line.indexOf("'");
            int singleRightCount = line.lastIndexOf("'");
            //是否有花括号常量
            if (doubleLeftCount < doubleRightCount && doubleLeftCount < leftCount && leftCount < doubleRightCount) {

            } else if(singleLeftCount < singleRightCount && singleLeftCount < leftCount && leftCount < singleRightCount) {

            } else {
                if('{' == data) {
                    leftJurisdictionCount ++;
                } else {
                    rightJurisdictionCount++;
                }
            }

            leftCount = line.indexOf(data,leftCount + 1);
        }
    }

    /**
     * 方法是否结束
     */
    public boolean isMethodOver() {
        boolean methodOver = leftJurisdictionCount == rightJurisdictionCount ? true : false;
        if (methodOver) {
            methodName = null;  //方法标识复位
            leftJurisdictionCount = 0;
            rightJurisdictionCount = 0;
            methodCount = null; // help gc
        }

        return methodOver;
    }

    /**
     * 将方法数据封装到相应的缓存
     */
    public void saveMethodMsg(String line) {
        if (null == methodCount) {
            return;
        }

        methodCount.setLineAllNumber(methodCount.getLineAllNumber() + 1);
    }

    /**
     * 统计所扫描的每个方法的行数
     */
    public void methodCountLine(String line) {
        if (null == methodName || null == line) {
           return;
        }

        //进行方法数据的统计
        saveMethodMsg(line);
        countMethodRegx(line,'{');
        countMethodRegx(line,'}');
        isMethodOver(); //该方法是否结束
    }

    @Override
    public void forCount(String nowLine) {
        int liftPos = nowLine.indexOf("for");  //查找for语句
        int rightPos = nowLine.lastIndexOf("{");

        if (-1 != liftPos && -1 != rightPos && liftPos < rightPos)
            javaResult.setForNumber(javaResult.getForNumber() + 1);
    }

    @Override
    public void whileCount(String nowLine) {
        int liftPos = nowLine.indexOf("while");  //查找for语句
        int rightPos = nowLine.lastIndexOf("{");

        if (-1 != liftPos && -1 != rightPos && liftPos < rightPos)
            javaResult.setWhileNumber(javaResult.getWhileNumber() + 1);
    }

    @Override
    public Integer codeCount() throws IOException {
        javaResult = (SourceCount) RootScanResult.getCountResult().get(COUNT_TEMPLATE_FLAG_JAVA);
        if (null == javaResult)
            throw new IOException();

        javaResult.setSourceFileNumber(javaResult.getSourceFileNumber() + 1);

        BufferedReader bufferedReader = openFileStream(codeFile);
        int lineNumber = 0;
        String line = null;
        while (null != (line = bufferedReader.readLine())) {

            ifCount(line); //if统计
            forCount(line); //for统计
            whileCount(line); //while统计
            methodCount(line); //函数统计
            methodCountLine(line); //函数行数统计

            lineNumber++; //所有行相加
        }
        bufferedReader.close();
        javaResult.setLineAllNumber(javaResult.getLineAllNumber() + lineNumber);
        return 0;
    }


    @Override
    public String isCountType(String type) {
        if (COUNT_TEMPLATE_FLAG_JAVA.equals(type)) {
            return COUNT_TEMPLATE_FLAG_JAVA;
        }

        return null;
    }

    @Override
    public void setFile(File codeFile) {
        this.codeFile = codeFile;
    }
}
