package com.zihao.util;


import com.zihao.bean.RootScanResult;
import com.zihao.bean.SourceCount;
import com.zihao.bean.MethodCount;
import com.zihao.conf.ScanBeforeConfig;
import com.zihao.count.Count;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 进行代码的扫描统计
 */
public class CodeScaning {

    /**
     * 检测是否为需要扫描的文件类型
     * @param fileName
     * @return
     */
    public static String isScanType(String fileName) {
        String substring = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        if(ScanBeforeConfig.scanType.contains(substring)) {
            return new String(substring);
        }

        return null;
    }

    /**
     * 取得该文件的创建日期
     * @param fileType
     * @param codeFile
     */
    private static void groupSourceFileByDate(String fileType,File codeFile) {
        if (null == fileType || null == codeFile) {
            return ;
        }

        /* 取出该源码文件的创建日期 */
        String sourceKey = SourceFileDateUtil.formatSourceDate(codeFile.lastModified());
        /* 取出缓存数据 */
        Map<String,List<File>> countGroupByDate = RootScanResult.getCountGroupByDate();
        if (countGroupByDate.containsKey(sourceKey)) {

            countGroupByDate.get(sourceKey).add(codeFile);
        } else {

            List<File> sourceCount = new ArrayList<>();
            sourceCount.add(codeFile);
            countGroupByDate.put(sourceKey,sourceCount);
        }
    }

    /**
     * 根据文件类型选择合适的扫描模板
     * @param fileType 源码文件后缀
     * @param codeFile 当前需要统计的源码文件
     * @param result   结果集
     * @throws IOException
     */
    private static void chooseTemplateScaning(String fileType, File codeFile,
                                              Map<String,Object> result) throws IOException {

        for(Map.Entry<String,Object>template : ScanBeforeConfig.countTemplate.entrySet()) {

            String tempTypeName = null;
            Count nowScanTemp = (Count)template.getValue();
            if(null != (tempTypeName = nowScanTemp.isCountType(fileType))) {
                /* 统计之前查看缓存是否有结果对象 */
                if(null == result.get(tempTypeName)) {
                    result.put(tempTypeName,new SourceCount());
                }
                /* 取当前源码文件进行日期分类,后期可能需要把该方法放到语言统计模板类中 */
                groupSourceFileByDate(fileType,codeFile);
                nowScanTemp.setFile(codeFile);
                nowScanTemp.codeCount();
                break;
            }
        }
    }

    /**
     * 具体分析当前遍历的目录
     * @param codeFile 当前遍历的源码文件
     * @param result   结果集
     */
    public static void countCode(File codeFile,Map<String,Object> result) {
        String fileType = null;
        if ((fileType = isScanType(codeFile.getName())) == null)
            return; //不是需要统计的文件

        try {
            System.out.println("now Scaning file name is:" + codeFile.getName());
            Thread.sleep(new Random().nextInt(3) * 10);
            chooseTemplateScaning(fileType,codeFile,result);
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("文件异常");
        }
    }

    /**
     * 文件开始遍历
     * @param fileMsg 根目录
     */
    public static void recursion(File fileMsg) {
        File[] files = fileMsg.listFiles();

        for (File nowFile : files) {
            if (nowFile.isDirectory()) {
                recursion(nowFile);
            } else {
                countCode(nowFile,RootScanResult.getCountResult());
            }
        }
    }

    /**
     * 判断目标项目是否存在
     */
    public static void run() {
        File msgFile = new File(ScanBeforeConfig.rootPath);
        if (!msgFile.exists() || !msgFile.isDirectory()) {
            throw new RuntimeException("路径不存在");
        }
        recursion(msgFile);
    }


    /**
     * 项目分析
     */
    public static void beginScan() {
        System.out.println("正在搜索源码文件..........");
        try {
            TimeUnit.SECONDS.sleep(3);
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("正在扫描............\n");
        run();
        System.out.println("\n扫描结束............\n");
    }
}
