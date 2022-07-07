package com.sxl.beangeneratorsxl.parser.excel;

/**
 * @author qiancheng-su
 * @description: excel表头属性
 * @date 2022/7/6 13:30
 */
public enum ExcelHeadEnum {

    /**
     * 类成员变量名称
     */
     FIELD_NAME("filedName"),

    /**
     * 类成员变量类型
     */
    FIELD_TYPE("filedType"),

    /**
     * 类成员变量说明
     */
    FIELD_NOTE("filedNote"),

    /**
     * 类成员 约束，如非空，最大长度等
     */
    FIELD_CONSTRAINT("filedConstraint");



    ExcelHeadEnum(String name){
        this.name = name;
    }

    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
