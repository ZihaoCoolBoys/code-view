package com.zihao.out;

import com.zihao.bean.MethodCount;
import com.zihao.bean.RootScanResult;
import com.zihao.bean.SourceCount;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author zi hao
 * @version 1.0
 * @date 2020/9/20 17:46
 *
 * Excel格式数据输出
 */

public class ExcelOut extends OutCountResult{

    private Workbook workbook;
    private Sheet methodSheet;    //存放方法的统计信息
    private Sheet sourceCountSheet;    //代码的总计信息

    private  Integer methodCountLine = 0;
    private  Integer methodNumber = 0;
    private  Integer maxLengthMethod = 0;

    public ExcelOut(FileOutputStream fileOutputStream) {
        super(fileOutputStream);
        this.workbook = new XSSFWorkbook();
    }

    /**
     * 工作薄初始化
     */
    private void createSheet() {
        this.sourceCountSheet = workbook.createSheet("项目工程总计信息");
        this.methodSheet = workbook.createSheet("项目方法统计信息");
    }

    private void createRowBody() {
        String []title = new String[]{"当前语言类型", "源码文件数量", "代码总行数" ,
                "if语句数量","for语句数量","while语句数量",
                "函数数量","最长的方法行数","平均一个方法行数"};

        Row rowTitle = this.sourceCountSheet.createRow(0);
        for (int i = 0 ; i < title.length ; i ++) {
            rowTitle.createCell(i).setCellValue(title[i]);
        }
    }

    /**
     * 写出整体统计
     */
    private void writeSourceMsg() {
        for (Map.Entry<String, Object> nowResult : RootScanResult.getCountResult().entrySet()) {

            SourceCount result = (SourceCount) nowResult.getValue();

            createRowBody();
            Row rowBody = this.sourceCountSheet.createRow(1);
            rowBody.createCell(0).setCellValue(nowResult.getKey());
            rowBody.createCell(1).setCellValue(result.getSourceFileNumber());
            rowBody.createCell(2).setCellValue(result.getLineAllNumber());
            rowBody.createCell(3).setCellValue(result.getIfNumber());
            rowBody.createCell(4).setCellValue(result.getForNumber());
            rowBody.createCell(5).setCellValue(result.getWhileNumber());
            rowBody.createCell(6).setCellValue(result.getMethodNumber());
        }

    }

    //统计最长的方法代码长度
    private void countMaxLenthMethod(Integer methodLen) {
        maxLengthMethod = methodLen > maxLengthMethod ? methodLen : maxLengthMethod;
    }

    /**
     * 写出方法的统计
     */
    private void writeMethodMsg() {
        Row rowOne = this.methodSheet.createRow(0); //记录方法名称

        rowOne.createCell(0).setCellValue("方法名称");
        rowOne.createCell(1).setCellValue("方法行数");
        rowOne.createCell(2).setCellValue("方法位置");

        int lines = 0;
        for (Map.Entry<String, List<MethodCount>> datas : RootScanResult.getCountMethodResult().entrySet()) {

            List<MethodCount> methodCounts = datas.getValue();
            for (MethodCount methodCount : methodCounts) {
                Row rowNow = this.methodSheet.createRow(++lines); //记录方法行数
                rowNow.createCell(0).setCellValue(methodCount.getMethodName());
                rowNow.createCell(1).setCellValue(methodCount.getLineAllNumber());
                rowNow.createCell(2).setCellValue(datas.getKey());
                methodNumber++;
                methodCountLine += methodCount.getLineAllNumber();
                countMaxLenthMethod(methodCount.getLineAllNumber());
            }
        }

        this.sourceCountSheet.getRow(1).createCell(7).setCellValue(maxLengthMethod);
        this.sourceCountSheet.getRow(1).createCell(8).setCellValue(methodCountLine / methodNumber);
    }

    /**
     * 保存Excel文件
     */
    private void saveExcel() {
        try {
            workbook.write(getOutputStream());
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                getOutputStream().close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    void print() {
        createSheet();
        writeSourceMsg();
        writeMethodMsg();
        saveExcel();
    }
}
