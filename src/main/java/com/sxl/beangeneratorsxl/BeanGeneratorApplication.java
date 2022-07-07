package com.sxl.beangeneratorsxl;

import com.sxl.beangeneratorsxl.builder.JavaFileOutput;
import com.sxl.beangeneratorsxl.builder.JavaOutput;
import com.sxl.beangeneratorsxl.config.AppConfig;
import com.sxl.beangeneratorsxl.config.BeanConfig;
import com.sxl.beangeneratorsxl.generator.JavaClassGenerator;
import com.sxl.beangeneratorsxl.parser.definition.JavaDefinition;
import com.sxl.beangeneratorsxl.parser.excel.ExcelHeadEnum;
import com.sxl.beangeneratorsxl.parser.json.FastJsonParser;
import com.sxl.beangeneratorsxl.parser.json.IJsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.HashMap;

/**
 * @author qiancheng-su
 */
@SpringBootApplication
@Slf4j
public class BeanGeneratorApplication {

    public static void main(String[] args) {
        generateWithExcelDir(args);
    }

    public static void generateWithExcelDir(String[] args){
        try {
            // 1. 配置应用信息
            AppConfig appConfig = new AppConfig();
            appConfig.setSwaggerEnable(true);
            appConfig.setLombokEnable(true);
            String outputPath = "E:\\sxl-project\\bean-generator-sxl-ws\\bean-generator-sxl\\src\\main\\java";
            appConfig.setOutPath(outputPath);

            // 2. 配置类的相关信息
            BeanConfig beanConfig = new BeanConfig();
            // 2.1 配置基础信息
            String packageName = "com.sxl.beangeneratorsxl.pojo";
            beanConfig.setPackageName(packageName);
            beanConfig.setCreator("sxl");

            // 2.2 配置excel对应列的信息
            String dirPath = "E:\\code_birth_plcace\\source\\main_online_inspection";
            HashMap<ExcelHeadEnum,Integer> excelHeadIndexMap = new HashMap<>();
            excelHeadIndexMap.put(ExcelHeadEnum.FIELD_NAME,0);
            excelHeadIndexMap.put(ExcelHeadEnum.FIELD_TYPE,1);
            excelHeadIndexMap.put(ExcelHeadEnum.FIELD_NOTE,2);
            beanConfig.setExcelHeadIndexMap(excelHeadIndexMap);

            JavaClassGenerator generator = new JavaClassGenerator();
            generator.generateByExcelDir(dirPath,appConfig,beanConfig);
        }catch (Exception e){
            log.error("错误异常信息:",e);
            System.exit(1);
        }
    }

    public static void generateWithJsonFile(String[] args){
        try {
            AppConfig appConfig = new AppConfig();
            BeanConfig beanConfig = new BeanConfig();
            String packageName = "com.sxl.test";
            beanConfig.setPackageName(packageName);
            beanConfig.setCreator("sxl");
            IJsonParser jsonParser = new FastJsonParser(appConfig,beanConfig);
            File file = new File("");
            JavaDefinition root = jsonParser.parseJson(file);
            JavaOutput output = new JavaFileOutput(appConfig);
            output.print(root);
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

}
