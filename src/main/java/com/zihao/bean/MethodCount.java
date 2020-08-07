package com.zihao.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 方法的名称:源代码的绝对路径 + 方法名称
 * 没有把它放在SourceCount类中
 * 原因1:SourceCount里面统计了一个源码文件的信息
 *      而一个源码文件可以有很多的方法
 * 原因2:为了进行项目中方法的优化,需要手动的定位,或者自动的定位方法的具体位置
 */

@Getter
@Setter
@ToString
@Data
public class MethodCount extends BaseCount {

    private String methodName;

}
