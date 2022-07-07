package com.sxl.beangeneratorsxl.generator;

import com.sxl.beangeneratorsxl.builder.JavaFileOutput;
import com.sxl.beangeneratorsxl.builder.JavaOutput;
import com.sxl.beangeneratorsxl.config.AppConfig;
import com.sxl.beangeneratorsxl.config.BeanConfig;
import com.sxl.beangeneratorsxl.parser.definition.JavaDefinition;
import com.sxl.beangeneratorsxl.parser.excel.ExcelParser;
import com.sxl.beangeneratorsxl.parser.excel.IExcelParser;
import com.sxl.beangeneratorsxl.parser.json.FastJsonParser;
import com.sxl.beangeneratorsxl.parser.json.IJsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiancheng-su
 * @description: Java类生成器
 * @date 2022/7/7 9:26
 */
@Slf4j
public class JavaClassGenerator implements IJavaClassGenerator {

    @Override
    public void generateByExcelDir(String dirPath, AppConfig appConfig, BeanConfig beanConfig) throws Exception {
        List<File> fileList = getFileList(dirPath);
        if(!CollectionUtils.isEmpty(fileList)){
            for(File file:fileList){
                beanConfig.setFile(file);
                log.info("开始解析文件：{}",file.getName());
                parseExcelToGenJavaClass(appConfig,beanConfig);
                log.info("文件{}解析完成！",file.getName());
            }
            log.info("累计解析文件：{}个",fileList.size());
        }


    }

    @Override
    public void generateByExcelPath(String excelPath, AppConfig appConfig, BeanConfig beanConfig) throws Exception {
        File file = new File(excelPath);
        beanConfig.setFile(file);
        log.info("开始解析文件：{}",file.getName());
        parseExcelToGenJavaClass(appConfig,beanConfig);
        log.info("文件{}解析完成！",file.getName());
    }

    @Override
    public void generateByJsonDir(String dirPath, AppConfig appConfig, BeanConfig beanConfig) throws Exception {
        List<File> fileList = getFileList(dirPath);
        if(!CollectionUtils.isEmpty(fileList)){
            for(File file:fileList){
                beanConfig.setFile(file);
                log.info("开始解析文件：{}",file.getName());
                parseJsonToGenJavaClass(appConfig,beanConfig);
                log.info("文件{}解析完成！",file.getName());
            }
            log.info("累计解析文件：{}个",fileList.size());
        }
    }

    @Override
    public void generateByJsonPath(String filePath, AppConfig appConfig, BeanConfig beanConfig) throws Exception {
        File file = new File(filePath);
        beanConfig.setFile(file);
        parseJsonToGenJavaClass(appConfig,beanConfig);
    }


    private void parseExcelToGenJavaClass(AppConfig appConfig, BeanConfig beanConfig) throws Exception {
        IExcelParser excelParser = new ExcelParser(appConfig,beanConfig);
        JavaDefinition root = excelParser.parseExcel(beanConfig.getFile());
        JavaOutput output = new JavaFileOutput(appConfig);
        output.print(root);
    }


    private List<File> getFileList(String dirPath){
        List<File> fileList = new ArrayList<>();
        File dir = new File(dirPath);
        String[] fileNameList = dir.list();
        if(null == fileNameList){
            log.error("{}:下的文件列表为空",dirPath);
            return fileList;
        }
        String filePath="";
        for(String fileName:fileNameList){
            if(dirPath.endsWith(File.separator)){
                filePath = dirPath+fileName;
            }else {
                filePath = dirPath+File.separator+fileName;

            }
            File file = new File(filePath);
            fileList.add(file);
        }
        return fileList;
    }


    private void parseJsonToGenJavaClass(AppConfig appConfig,BeanConfig beanConfig) throws Exception {
        IJsonParser jsonParser = new FastJsonParser(appConfig,beanConfig);
        JavaDefinition root = jsonParser.parseJson(beanConfig.getFile());
        JavaOutput output = new JavaFileOutput(appConfig);
        output.print(root);
    }
}
