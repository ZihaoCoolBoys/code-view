package com.zihao.out;

import com.zihao.bean.MethodCount;
import com.zihao.bean.RootScanResult;
import com.zihao.bean.SourceCount;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author zi hao
 * @version 1.0
 * @date 2020/9/20 16:46
 *
 * 控制台结果输出
 */

public class ConsoleOut extends OutCountResult {

    private static Integer methodCountLine = 0;
    private static Integer methodNumber = 0;
    private static Integer maxLengthMethod = 0;

    public ConsoleOut(OutputStream outputStream) {
        super(outputStream);
    }

    /**
     * 控制台结果打印
     */
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

    /**
     * 统计最长的方法代码长度
     * @param methodLen 当前方法长度
     */
    private void countMaxLengthMethod(Integer methodLen) {
        maxLengthMethod = methodLen > maxLengthMethod ? methodLen : maxLengthMethod;
    }

    /**
     * 显示方法统计信息
     */
    private void showMethodResult() {
        Map<String, Object> countResult = RootScanResult.getCountResult();
        RootScanResult.getCountMethodResult();

        System.out.println("\nConsole>> 以下是你函数的书写情况");
        for (Map.Entry<String, List<MethodCount>> nowResult : RootScanResult.getCountMethodResult().entrySet()) {

            try{
                Thread.sleep(new Random().nextInt(20) * 10);
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
                countMaxLengthMethod(nowMethodResult.getLineAllNumber());
            }
            System.out.println("Console>>\n");
        }
        System.out.println("Console>> ----------------------------------------------------\n");
    }

    /**
     * 显示方法行数统计信息
     */
    private void showMethodCount() {
        System.out.println("Console>> 最长的方法>> " + maxLengthMethod + "行");
        System.out.println("Console>> 平均一个方法>> " + methodCountLine / methodNumber + "行");
    }

    @Override
    void print() {
        showMethodResult();
        showScanResult();
        showMethodCount();
    }
}
