package com.sxl.beangeneratorsxl.parser.definition;

/**
 * @author qiancheng-su
 * @description:
 * @date 2022/7/5 13:46
 */
public class BaseDefinition extends AbstractDefinition {

    public BaseDefinition(String name, String type) {
        super.name = name;
        super.packageName = type;
    }
}
