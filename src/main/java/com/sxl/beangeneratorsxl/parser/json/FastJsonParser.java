package com.sxl.beangeneratorsxl.parser.json;

import com.alibaba.fastjson.JSONObject;
import com.sxl.beangeneratorsxl.config.AppConfig;
import com.sxl.beangeneratorsxl.config.BeanConfig;
import com.sxl.beangeneratorsxl.parser.AbstractParser;
import com.sxl.beangeneratorsxl.parser.definition.AbstractDefinition;
import com.sxl.beangeneratorsxl.parser.definition.BaseDefinition;
import com.sxl.beangeneratorsxl.parser.definition.JavaDefinition;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author qiancheng-su
 * @description: FastJson解析器
 * @date 2022/7/5 14:20
 */
public class FastJsonParser extends AbstractParser implements IJsonParser {

    public FastJsonParser(AppConfig appConfig, BeanConfig beanConfig){
        super(appConfig,beanConfig);
    }

    @Override
    public JavaDefinition parseJson(File jsonFile) throws Exception {
        JavaDefinition javaBeanDefinition = new JavaDefinition();
        javaBeanDefinition.setPackageName(beanConfig.getPackageName());
        javaBeanDefinition.setName(beanConfig.getRootClassName());
        javaBeanDefinition.setNote(buildDftNote());
        doParse(javaBeanDefinition, jsonFile);
        return javaBeanDefinition;
    }

    @Override
    public void doParse(JavaDefinition rootDefinition, File jsonFile) throws Exception {
        String json = readJsonString(jsonFile);
        JSONObject jsonObject = JSONObject.parseObject(json);
        parseParent(rootDefinition,jsonObject);
    }

    private void parseParent(JavaDefinition pDefinition,JSONObject jsonObject){
        for(String key:jsonObject.keySet()){
            // 设置类的成员变量名称
            Object filed = jsonObject.get(key);
            JavaDefinition.FieldDefinition fieldDefinition = new JavaDefinition.FieldDefinition();
            String fieldName = formatFieldName(key);
            fieldDefinition.setFieldName(fieldName);

            // 设置类的成员变量注解
            if (!key.equals(fieldName)) {
                fieldDefinition.addAnnotation(getJsonFieldAnntotation(key));
                pDefinition.addImport("com.alibaba.fastjson.annotation.JSONField");
            }

            // 判断是否为list类型
            if(filed instanceof List){
            //  fieldName = pluralToSingular(getJsonFieldAnntotation(key));
                fieldDefinition.setArray(true);
                pDefinition.addImport("java.util.List");
                List jsonArray= (List) filed;
                if(jsonArray.size() ==0){
                    filed = new Object();
                }else {
                    filed = jsonArray.get(0);
                }
            }

            // 设置类的成员变量依赖的包文件
            AbstractDefinition type = getFieldType(fieldName,filed);
            if(type.getPackageName() !=null && !"".equals(type.getPackageName())
                    && !pDefinition.getPackageName().equals(type.getPackageName())){
                pDefinition.addImport(type.getPackageName()+"."+type.getName());
            }

            fieldDefinition.setType(type);
            pDefinition.addField(fieldDefinition);
        }

    }

    /**
     * @description: 验证属性名称是否合法
     * @author qiancheng-su
     * @return: {@link boolean}
     * @date: 2022/7/5 15:00
     */
    private boolean fieldNameValidate(Set<String> keys) {
        for (String key : keys) {
            if (!key.matches("^[a-zA-Z][a-zA-Z0-9_]*$")) {
                return false;
            }
        }

        return true;
    }

    /**
     * @description: 获取字段的类型
     * @author qiancheng-su
     * @return: {@link AbstractDefinition}
     * @date: 2022/7/5 14:57
     */
    private AbstractDefinition getFieldType(String fieldName, Object field) {
        if (field instanceof JSONObject) {
            // 校验字段名称是否符合规范
            if (!fieldNameValidate(((JSONObject) field).keySet())) {
                //todo:value的类型？
                return new BaseDefinition("Map", "java.util");
            } else {
                JavaDefinition sDefinition = new JavaDefinition();
                sDefinition.setName(formatClassNameByField(fieldName));
                sDefinition.setPackageName(getDftPackage());
                sDefinition.setNote(buildDftNote());
                // 递归获取子属性的值
                parseParent(sDefinition, (JSONObject) field);
                return sDefinition;
            }

        } else if (field instanceof String) {
            return new BaseDefinition("String", "");
        } else if (field instanceof Number) {
            if (field instanceof Integer) {
                return new BaseDefinition("int", "");
            } else if (field instanceof Long) {
                return new BaseDefinition("long", "");
            } else if (field instanceof Float) {
                return new BaseDefinition("float", "");
            } else if (field instanceof Short) {
                return new BaseDefinition("short", "");
            } else {
                return new BaseDefinition("double", "");
            }
        } else if (field instanceof Date) {
            return new BaseDefinition("Date", "java.util");
        } else if (field instanceof Boolean) {
            return new BaseDefinition("boolean", "");
        } else {
            //不支持的类型，存为object
            return new BaseDefinition("Object", "");
        }
    }


    private String getJsonFieldAnntotation(String paramName) {
        return String.format("@JSONField(name = \"%s\")", paramName);
    }

    /**
     * @description:
     * @author qiancheng-su
     * @return: {@link String}
     * @date: 2022/7/5 14:11
     */
    private String readJsonString(File jsonFile) throws Exception{
        InputStream in = new FileInputStream(jsonFile);
        try{
            InputStreamReader inReader = new InputStreamReader(in);
            BufferedReader bfReader = new BufferedReader(inReader);
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line= bfReader.readLine()) !=null){
                sb.append(line);
            }
            bfReader.close();
            inReader.close();
            return sb.toString();
        }finally {
            in.close();
        }
    }

}
