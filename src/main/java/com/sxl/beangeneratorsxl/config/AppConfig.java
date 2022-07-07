package com.sxl.beangeneratorsxl.config;

import com.beust.jcommander.converters.FileConverter;
import com.sxl.beangeneratorsxl.parser.excel.ExcelHeadEnum;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.HashMap;


/**
 * description:
 * @author qiancheng-su
 * @Param:  * @param null
 * @return: {@link null}
 * @date: 2022/7/5 13:28
 */
@Data
public class AppConfig {
    /**
     * 输出地址: 一般项目的工程根目录
     */
    private String outPath = "";

    /**
     * json 解析框架
     */
    private String frame;

    /**
     * 是否启用lombok，默认开启
     */
    private boolean lombokEnable;

    /**
     * 是否启用swagger，默认开启
     */
    private boolean swaggerEnable;

    public AppConfig(){
        this.frame = "fastjson";
        this.swaggerEnable = false;
        this.lombokEnable = true;
    }

    public static class ExistFileConverter extends FileConverter{
        @Override
        public File convert(String value) {
            File file = super.convert(value);
            if(file == null || !file.exists() || !file.isFile() ){
                return null;
            }
            return file;
        }
    }





}
