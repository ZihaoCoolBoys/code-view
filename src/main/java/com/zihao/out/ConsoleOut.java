package com.zihao.out;

import com.zihao.bean.MethodCount;
import com.zihao.bean.RootScanResult;
import com.zihao.bean.SourceCount;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 控制台结果输出
 */

public class ConsoleOut extends OutCountResult {

    private static Integer methodCountLine = 0;
    private static Integer methodNumber = 0;
    private static Integer maxLentghMethod = 0;

    public ConsoleOut(OutputStream outputStream) {
        super(outputStream);
    }

    //显示结果
    private void showScanResult() {

        System.out.println("Console>> 以下是你目前的代码情况");
        for (Map.Entry<String, Object> nowResult : RootScanResult.getCountResult().entrySet()) {

            System.out.println("Console>> ----------------------------------------------------");
            SourceCount result = (SourceCount) nowResult.getValue();
            System.out.println("Console>> 当前语言源文件类型[ " + nowResult.getKey() + " ]");
            System.out.println("Console>> 目前为止共写[ " + result.getSourceFileNumber() + " ]个源码文件");
            System.out.println("Console>> 目前为止共写[ " + result.getLineAllNumber() + " ]行");
            System.out.println("Console>> 其中共使用[ " + result.getIfNumber() + " ]个if语句");
            System.out.println("Console>> 其中共使用[ " + result.getForNumber() + " ]个for语句");
            System.out.println("Console>> 其中共使用[ " + result.getWhileNumber() + " ]个while语句");
            System.out.println("Console>> 其中共写[ " + result.getMethodNumber() + " ]个函数");
            System.out.println("Console>> ----------------------------------------------------");
        }
    }

    //统计最长的方法代码长度
    private void countMaxLenthMethod(Integer methodLen) {
        maxLentghMethod = methodLen > maxLentghMethod ? methodLen : maxLentghMethod;
    }

    //显示方法统计信息
    private void showMethodResult() {

        Map<String, Object> countResult = RootScanResult.getCountResult();
        RootScanResult.getCountMethodResult();

        System.out.println("\nConsole>> 以下是你函数的书写情况");
        for (Map.Entry<String, List<MethodCount>> nowResult : RootScanResult.getCountMethodResult().entrySet()) {

            try{
                //Thread.sleep(new Random().nextInt(20) * 10);
            }catch (Exception e) {
            }

            System.out.println("Console>>");
            System.out.println("Console>> 源码全文件名>> " + nowResult.getKey());
            List<MethodCount> methodCounts = nowResult.getValue();
            for (MethodCount nowMethodResult : methodCounts) {
                methodNumber++;
                System.out.println("Console>> 方法名称>> " + nowMethodResult.getMethodName());
                System.out.println("Console>> 方法行数>> " + nowMethodResult.getLineAllNumber());
                methodCountLine += nowMethodResult.getLineAllNumber();
                countMaxLenthMethod(nowMethodResult.getLineAllNumber());
            }
            System.out.println("Console>>\n");
        }
        System.out.println("Console>> ----------------------------------------------------\n");
    }

    //显示方法行数统计信息
    private void showMethodCount() {
        System.out.println("Console>> 最长的方法>> " + maxLentghMethod + "行");
        System.out.println("Console>> 平均一个方法>> " + methodCountLine / methodNumber * 3 + "行");
    }

    @Override
    void print() {

        showMethodResult();
        showScanResult();
        showMethodCount();
    }
}
