package com.sxl.beangeneratorsxl.parser;

import com.sxl.beangeneratorsxl.config.AppConfig;
import com.sxl.beangeneratorsxl.config.BeanConfig;
import com.sxl.beangeneratorsxl.parser.definition.JavaDefinition;
import lombok.Data;

import java.io.File;
import java.util.Date;

/**
 * @author qiancheng-su
 * @description: Json解析类
 * @date 2022/7/5 13:55
 */
@Data
public abstract class AbstractParser  {
    protected AppConfig appConfig;

    protected BeanConfig beanConfig;


    public AbstractParser(AppConfig appConfig,BeanConfig beanConfig) {
        this.appConfig = appConfig;
        this.beanConfig = beanConfig;
    }

    public abstract void doParse(JavaDefinition rootDefinition, File file) throws Exception;


    protected String getDftPackage(){
        return beanConfig.getPackageName();
    }



    protected String buildDftNote(){
        return String.format("/**\n" +
                " * @date: %tF \n" +
                " * @author: " +beanConfig.getCreator()+" \n" +
                " */", new Date());
    }

    /**
     * javabean规范格式化field
     * @param fieldName
     * @return
     */
    protected String formatFieldName(String fieldName){
        // 替换掉所有的空格
        fieldName = fieldName.replaceAll("\\s+","");
        if(fieldName.matches("[A-Z]*")){//都是大写
            return fieldName.toLowerCase();
        } else if(fieldName.length() > 1 && fieldName.substring(0, 2).matches("[A-Z]*")){
            //首两个字母要么大写要么小写
            return fieldName.substring(0, 2).toLowerCase() + fieldName.substring(2);
        } else if('A' <= fieldName.charAt(0) && 'Z' >= fieldName.charAt(0)){
            return fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
        }
        return fieldName;
    }


    /**
     * 格式化类名
     * @param fieldName
     * @return
     */
    protected String formatClassNameByField(String fieldName){
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }



    /**
     * 复数变单数
     * @param name
     * @return
     */
    protected String pluralToSingular(String name){
        if(name.endsWith("ies")){
            return name.substring(0, name.length() - 3) + "y";
        } else if(name.endsWith("ses")){
            return name.substring(0, name.length() - 2) ;
        } else if(name.endsWith("s")){
            return name.substring(0, name.length() - 1);
        }

        return name;
    }


}
