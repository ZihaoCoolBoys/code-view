package com.zihao.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Data
public class BaseCount {
    //扫描的全部代码总行数
    protected  Integer lineAllNumber = 0;
}
