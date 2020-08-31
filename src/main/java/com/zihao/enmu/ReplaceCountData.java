package com.zihao.enmu;

/**
 * @author zi hao
 * @version 1.0
 * @date 2020/8/30 13:44
 *
 * 枚举标识,替换网页中需要显示的数据
 *
 */

public enum ReplaceCountData {

    LANGUAGE_COUNT("${languageCount}"),
    METHOD_COUNT("${methodCount}"),
    CODE_LINE_NUMBER("${codeLineNumber}");

    private String replaceName;

    ReplaceCountData(String argName) {

        this.replaceName = argName;
    }

    public String getReplaceName() {
        return replaceName;
    }
}
