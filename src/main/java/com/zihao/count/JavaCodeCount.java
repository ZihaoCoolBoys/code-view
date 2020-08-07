package com.zihao.count;

import java.io.*;

/**
 * java语言统计模式
 */

public interface JavaCodeCount extends Count {

    //if使用情况
    void ifCount(String nowLine);

    //方法统计
    void methodCount(String nowLine);

    //for语句统计
    void forCount(String nowLine);

    //while语句统计
    void whileCount(String nowLine);

    default BufferedReader openFileStream(File codeFile) throws FileNotFoundException {
        if(null == codeFile || codeFile.exists() == false)
            throw new RuntimeException("文件异常");

        return new BufferedReader(new FileReader(codeFile));
    }
}
