package com.sxl.beangeneratorsxl.config;

import com.sxl.beangeneratorsxl.parser.excel.ExcelHeadEnum;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.HashMap;

/**
 * @author qiancheng-su
 * @description: 生成Bean的配置类
 * @date 2022/7/7 9:41
 */
@Data
public class BeanConfig {

    /**
     * 解析的文件
     */
    private File file;


    private HashMap<ExcelHeadEnum,Integer> excelHeadIndexMap;

    /**
     * 类所在的包名
     */
    private String packageName;

    /**
     * 生成的类名称
     */
    private String rootClassName;

    /**
     * 代码作者
     */
    private String creator;


    public BeanConfig(){
        this.packageName ="com.sxl.pojo";
        this.rootClassName = "RootClass";
        this.creator = "qiancheng-su";

    }

    public void setFile(File file){
        this.file = file;
        this.rootClassName = formatClassName(file.getName().substring(0,file.getName().lastIndexOf('.')));
    }

    public void setRootClassName(String className){
        this.rootClassName = className;
    }


    /**
     * @description: 标准化类命名
     * @author qiancheng-su
     * @return: {@link String}
     * @date: 2022/7/7 9:44
     */
    private String formatClassName(String className){
        if (!className.matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
            return "RootClass";
        }

        if(!StringUtils.isEmpty(className) &&
                className.charAt(0) >='a' && className.charAt(0) <='z'){
            return className.substring(0, 1).toUpperCase() + className.substring(1);
        }else {
            return className;
        }
    }
}
