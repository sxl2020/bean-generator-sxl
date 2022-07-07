package com.sxl.beangeneratorsxl.parser.json;

import com.sxl.beangeneratorsxl.parser.definition.JavaDefinition;

import java.io.File;

/**
 * @author qiancheng-su
 * @description: 文件解析接口
 * @date 2022/7/5 13:30
 */
public interface IJsonParser {

    JavaDefinition parseJson(File json) throws Exception;

}
