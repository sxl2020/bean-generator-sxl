package com.sxl.beangeneratorsxl.builder;


import com.sxl.beangeneratorsxl.parser.definition.JavaDefinition;

/**
 * @description:
 * @author qiancheng-su
 * @return: {@link null}
 * @date: 2022/7/5 15:31
 */
public interface JavaOutput {
    void print(JavaDefinition root) throws Exception;
}
