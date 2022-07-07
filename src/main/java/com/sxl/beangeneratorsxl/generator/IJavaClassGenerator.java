package com.sxl.beangeneratorsxl.generator;

import com.sxl.beangeneratorsxl.config.AppConfig;
import com.sxl.beangeneratorsxl.config.BeanConfig;

/**
 * @author qiancheng-su
 * @description: 生成Java类的功能接口
 * @date 2022/7/7 9:31
 */
public interface IJavaClassGenerator {

    void generateByExcelDir(String dirPath,  AppConfig appConfig, BeanConfig beanConfig) throws Exception;

    void generateByExcelPath(String excelPath, AppConfig appConfig, BeanConfig beanConfig) throws Exception;

    void generateByJsonDir(String dirPath, AppConfig appConfig, BeanConfig beanConfig) throws Exception;

    void generateByJsonPath(String dirPath, AppConfig appConfig, BeanConfig beanConfig) throws Exception;


}
