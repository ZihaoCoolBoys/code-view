package com.zihao.out;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.OutputStream;

/**
 * 结果输出
 */

@Setter
@Getter
@Data
public abstract class OutCountResult {

    private OutputStream outputStream;

    abstract void print() throws InterruptedException;

    public OutCountResult(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void showResult() throws InterruptedException {
        print();
    }

}
