package com.sxl.beangeneratorsxl.parser.excel;

import com.sxl.beangeneratorsxl.config.AppConfig;
import com.sxl.beangeneratorsxl.config.BeanConfig;
import com.sxl.beangeneratorsxl.parser.AbstractParser;
import com.sxl.beangeneratorsxl.parser.definition.AbstractDefinition;
import com.sxl.beangeneratorsxl.parser.definition.BaseDefinition;
import com.sxl.beangeneratorsxl.parser.definition.JavaDefinition;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Date;

/**
 * @author qiancheng-su
 * @description: excel解析器
 * @date 2022/7/6 8:56
 */
@Slf4j
public class ExcelParser extends AbstractParser implements IExcelParser{


    private final String[] excel_row_name = new String[]{"名称","类型","最大长度","说明","示例值"};

    public ExcelParser(AppConfig appConfig, BeanConfig beanConfig) {
        super(appConfig,beanConfig);
    }


    @Override
    public JavaDefinition parseExcel(File excelFile) throws Exception {
        JavaDefinition rootDefinition = new JavaDefinition();
        // 设置类的相关信息
        rootDefinition.setPackageName(beanConfig.getPackageName());
        rootDefinition.setName(beanConfig.getRootClassName());
        rootDefinition.setNote(buildDftNote());
        doParse(rootDefinition,excelFile);
        return rootDefinition;
    }

    @Override
    public void doParse(JavaDefinition rootDefinition, File excelFile) throws Exception {
        Workbook workbook = WorkbookFactory.create(excelFile);
        Sheet sheet = workbook.getSheetAt(0);
        parseSheet(rootDefinition,sheet);
    }


    private void parseSheet(JavaDefinition pDefinition,Sheet sheet) throws Exception {
        if(null == sheet){
            log.error("sheet为null");
            throw new Exception("sheet为null");
        }

        for(int rowIndex=sheet.getFirstRowNum() + 1;rowIndex<sheet.getLastRowNum();rowIndex++){
            JavaDefinition.FieldDefinition fieldDefinition = new JavaDefinition.FieldDefinition();
            Row attributeRow = sheet.getRow(rowIndex);
            // 1.设置属性名
            Cell nameCell = attributeRow.getCell(beanConfig.getExcelHeadIndexMap().get(ExcelHeadEnum.FIELD_NAME));
            String originalName = nameCell.getStringCellValue();
            String filedName = formatFieldName(nameCell.getStringCellValue());
            fieldDefinition.setFieldName(filedName);
            // 1.1 为字段添加注解
            if (!originalName.equals(filedName)) {
                fieldDefinition.addAnnotation(getJsonFieldAnnotation(originalName));
                pDefinition.addImport("com.alibaba.fastjson.annotation.JSONField");
            }

            if(appConfig.isLombokEnable()){
                pDefinition.addImport("lombok.Data");
            }

            // 2.设置类型
            Cell typeCell = attributeRow.getCell(beanConfig.getExcelHeadIndexMap().get(ExcelHeadEnum.FIELD_TYPE));
            AbstractDefinition type = getFieldType(typeCell.getStringCellValue());
            fieldDefinition.setType(type);

            // 3. 设置字段的注释：两种方式可选：普通注释与Swagger注解
            int noteColumn = beanConfig.getExcelHeadIndexMap().getOrDefault(ExcelHeadEnum.FIELD_NOTE,-1);
            if(-1 != noteColumn ){
                Cell noteCell = attributeRow.getCell(beanConfig.getExcelHeadIndexMap().get(ExcelHeadEnum.FIELD_NOTE));
                String fieldNote = noteCell.getStringCellValue();
                if(!StringUtils.isEmpty(fieldNote)){
                    if(appConfig.isSwaggerEnable()){
                        fieldDefinition.addAnnotation(getSwaggerFieldAnnotation(fieldNote));
                        pDefinition.addImport("io.swagger.annotations.ApiModelProperty");
                    }else {
                        fieldDefinition.setNote(getNormalFiledNote(fieldNote));
                    }
                }
            }

            // 4. 将类成员的依赖进行导入
            if (type.getPackageName() != null && !"".equals(type.getPackageName()) && !pDefinition.getPackageName().equals(type.getPackageName())) {
                pDefinition.addImport(type.getPackageName() + "." + type.getName());
            }
            pDefinition.addField(fieldDefinition);
        }

    }

    /**
     * @description: 获取字段的类型
     * @author qiancheng-su
     * @return: {@link AbstractDefinition}
     * @date: 2022/7/5 14:57
     */
    private AbstractDefinition getFieldType( String field) {
        switch (field.toLowerCase()) {
            case "string":
                return new BaseDefinition("String", "");
            case "int":
                return new BaseDefinition("int", "");
            case "long":
                return new BaseDefinition("long", "");
            case "float":
                return new BaseDefinition("float", "");
            case "short":
                return new BaseDefinition("short", "");
            case "double":
                return new BaseDefinition("double", "");
            case "date":
                return new BaseDefinition("Date", "java.util");
            case "boolean":
                return new BaseDefinition("boolean", "");
            default:
                //不支持的类型，存为object
                return new BaseDefinition("Object", "");
        }
    }


    private String getNormalFiledNote(String note){
        return String.format("/**\n" +
                "       * " +note+" \n" +
                "       */");
    }

    private String getSwaggerFieldAnnotation(String paramName) {
        return String.format("@ApiModelProperty(value = \"%s\")", paramName);
    }

    private String getJsonFieldAnnotation(String paramName) {
        return String.format("@JSONField(name = \"%s\")", paramName);
    }

}
