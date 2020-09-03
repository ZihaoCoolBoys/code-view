package com.zihao.util;

import java.io.*;

/**
 * @author zi hao
 * @version 1.0
 * @date 2020/9/3 10:37
 *
 * 负责资源文件的写出,数据的更换,文件的操作
 */

public class ResourceFile {

    /* 资源文件根路径 */
    private final String rootPath
            = this.getClass().getClassLoader().getResource("").getPath();

    /* 将需要动态替换的资源文件全部加载 */
    private  StringBuilder htmlBuf;
    private  StringBuilder codeViewBuf;

    public ResourceFile() {

        initData();
    }

    /**
     * 将资源文件读入 builder 缓存中
     */
    public void initData() {

        try {

            htmlBuf = loadResourceCaseStr(rootPath + "static/count.html");
            codeViewBuf = loadResourceCaseStr(rootPath + "static/code-view.js");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换网页部分总共代码的行数
     * @param replaceSource 要替换的数据
     * @param data  替换的数据
     * @param <T>   任意类型,公共方法,方便后期扩展
     */
    public <T> void replaceHtmlData(String replaceSource,T data) {

        /* 寻找需要替换的位置 */
        int replaceIndex = -1;
        /* 替换codeLineNumber数据 */
        replaceIndex = htmlBuf.lastIndexOf(replaceSource);
        if (-1 != replaceIndex) {

            htmlBuf.replace(replaceIndex
                    ,replaceIndex + replaceSource.length()
                    ,String.valueOf(data));
        }
    }

    /**
     * 数据动态替换,替换简单数据,替换
     */
    public <T> void replaceCodeViewData(String replaceSource,T jsonData) {

        /* 寻找需要替换的位置 */
        int replaceIndex = -1;
        /* 替换languageCount数据 */
        replaceIndex = codeViewBuf.lastIndexOf(replaceSource);
        if (-1 != replaceIndex) {

            codeViewBuf.replace(replaceIndex
                    , replaceIndex + replaceSource.length()
                    , JsonUtil.getJson(jsonData));
        }
    }

    /**
     * 数据动态替换,
     * @param replaceSource
     * @param data
     */
    public void replaceCodeViewData(String replaceSource,String data) {

        /* 寻找需要替换的位置 */
        int replaceIndex = -1;
        /* 替换languageCount数据 */
        replaceIndex = codeViewBuf.lastIndexOf(replaceSource);
        if (-1 != replaceIndex) {

            codeViewBuf.replace(replaceIndex
                    , replaceIndex + replaceSource.length()
                    , data);
        }
    }

    /**
     * 统一资源写入/写出
     *
     * @param readPath
     * @param writePath
     */
    public void resourceReadAndWrite(String readPath, String writePath) {

        /* 打开需要载入的资源文件 */
        File resourcePath = new File(rootPath + readPath);

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
     * 将需要动态替换的数据加载进来
     * 读入文本进行传出
     */
    public StringBuilder loadResourceCaseStr(String path) throws Exception {

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
    public void writeResourceCaseFile(StringBuilder builder,String path) throws Exception {

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
     * 获取文件后缀名
     * @param file
     * @return
     */
    public String getFileSuffixName(File file) {

        if (null == file || !file.exists()) {
            return null;
        }

        String fileName = file.getName();
        String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
        return suffixName;
    }

    public StringBuilder getHtmlBuf() {
        return htmlBuf;
    }

    public void setHtmlBuf(StringBuilder htmlBuf) {
        this.htmlBuf = htmlBuf;
    }

    public StringBuilder getCodeViewBuf() {
        return codeViewBuf;
    }

    public void setCodeViewBuf(StringBuilder codeViewBuf) {
        this.codeViewBuf = codeViewBuf;
    }

}
