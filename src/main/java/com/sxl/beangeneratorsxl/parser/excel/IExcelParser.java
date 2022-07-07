package com.sxl.beangeneratorsxl.parser.excel;

import com.sxl.beangeneratorsxl.parser.definition.JavaDefinition;

import java.io.File;
import java.util.List;

/**
 * @author qiancheng-su
 * @description: excel解析
 * @date 2022/7/6 8:52
 */
public interface IExcelParser {

    JavaDefinition parseExcel(File excel) throws Exception;
}
