package com.zihao.count;

import java.io.File;
import java.io.IOException;

/**
 * 所有语言统计模式的顶层接口
 * 预留以后进行公共属性的存放
 */
public interface Count {

    /**
     * 是否为扫描的类型
     * 由于为了以后扩展扫描其他语言的源代码
     * 这里进行动态扫描当前源码文件所属类型
     * 根据类型动态选择扫描这个源码文件的类
     * @param type
     * @return
     */
    String isCountType(String type);

    /**
     * 设置当前所扫描的源代码所在的绝对路径
     * @param codeFile
     */
    void setFile(File codeFile);

    /**
     * 这个方法进行代码流程统计的
     * 之前考虑模板模式,但是有的语言语法不同
     * 为了扩展没有使用模板模式
     * @return
     * @throws IOException
     */
    Integer codeCount() throws IOException;
}
