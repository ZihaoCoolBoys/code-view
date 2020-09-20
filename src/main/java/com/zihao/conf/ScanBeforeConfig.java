package com.zihao.conf;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * 扫描之前的配置
 */
public class ScanBeforeConfig {

    //代码根路径
    public static String rootPath;
    //扫描哪种类型的代码文件
    public static Set<String> scanType = new HashSet<>();
    //存储了统计代码的模板
    public static Map<String,Object> countTemplate = new HashMap<>();

    /**
     * 加载统计所有语言的算法
     */
    public static void loadCountClass() {
        String packageName = "com.zihao.count.impl.";
        String path = ScanBeforeConfig.class.getClassLoader()
                .getResource("com/zihao/count/impl").getPath();

        File countFileImp = new File(path);
        for (File nowFile : countFileImp.listFiles()) {

            String fileName = nowFile.getName().substring(0,nowFile.getName().lastIndexOf('.'));
            String classPath = new String(packageName + fileName);
            try {
                Class<?>countTemplate = Class.forName(classPath);
                ScanBeforeConfig.countTemplate.put(fileName,countTemplate.newInstance());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置属性
     */
    public static void setProperties(Properties properties) {
        ScanBeforeConfig.rootPath = properties.getProperty("rootPath");
        String fileType = properties.getProperty("scanType");
        String[] split = fileType.split(",");

        for (String nowType : split) {
            ScanBeforeConfig.scanType.add(nowType);
        }
    }

    /**
     * 载入配置文件
     */
    public static void initConfig() {
        loadCountClass();   //加载统计的模板
        Properties properties = new Properties();   //加载配置
        try{
            String path = ScanBeforeConfig.class.getClassLoader().getResource("scan-config.properties").getPath();
            properties.load(new FileInputStream(path));
            setProperties(properties);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
