package com.sxl.beangeneratorsxl.parser.definition;

import lombok.Data;

import java.util.*;

/**
 * @author qiancheng-su
 * @description: Java对象基本信息
 * @date 2022/7/5 13:47
 */

@Data
public class JavaDefinition  extends AbstractDefinition{

    /**
     * import内容: 类对象需要import的包名
     */
    private Set<String> imports;

    /**
     * 注释
     */
    private String note;

    /**
     * 属性
     */
    private Map<String, FieldDefinition> fieldMap;

    public Set<String> getImports() {
        return imports;
    }

    /**
     * 字段属性定义
     */
    @Data
    public static class  FieldDefinition {
        /**
         * 字段名
         */
        private String fieldName;

        /**
         * 注解
         */
        private Set<String> annotations;

        /**
         * 注释
         */
        private String note;

        /**
         * 类型
         */
        private AbstractDefinition type;

        /**
         * 是否数组
         */
        private boolean array;

        public void addAnnotation(String annotation){
            if(this.annotations == null){
                this.annotations = new HashSet<String>();
            }

            this.annotations.add(annotation);
        }
    }



    public void addImport(String importStr){
        if(importStr == null){
            return;
        }

        if(this.imports == null){
            this.imports = new HashSet<String>();
        }

        this.imports.add(importStr);
    }


    public void addField(FieldDefinition field){
        if(this.fieldMap == null){
            this.fieldMap = new LinkedHashMap<>();
        }

        this.fieldMap.put(field.getFieldName(), field);
    }

}
