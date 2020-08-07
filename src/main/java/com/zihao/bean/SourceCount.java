package com.zihao.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 源码文件的统计结果
 */

@Data
@Getter
@Setter
@ToString
public class SourceCount extends BaseCount {

    //if使用情况
    private Integer ifNumber = 0;

    //扫描全部方法的数量
    private Integer methodNumber = 0;

    //for语句数量
    private Integer forNumber = 0;

    //while语句数量
    private Integer whileNumber = 0;

    //所扫描的源码文件数量
    protected  Integer sourceFileNumber = 0;
}
