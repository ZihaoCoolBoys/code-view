package com.zihao;

import com.zihao.conf.ScanBeforeConfig;
import com.zihao.out.ConsoleOut;
import com.zihao.util.CodeScaning;


/**
 * Hello world!
 * by:zi hao
 */
public class App {

    public static void main(String[] args) throws Exception {

        ScanBeforeConfig.initConfig();
        CodeScaning.beginScan();
        new ConsoleOut(System.out).showResult();
        //new ExcelOut(new FileOutputStream("d:\\count.xlsx")).showResult();
    }
}
