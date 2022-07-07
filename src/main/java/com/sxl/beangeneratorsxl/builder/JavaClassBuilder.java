package com.sxl.beangeneratorsxl.builder;

import com.sxl.beangeneratorsxl.config.AppConfig;
import com.sxl.beangeneratorsxl.parser.definition.BaseDefinition;
import com.sxl.beangeneratorsxl.parser.definition.JavaDefinition;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.HashMap;

/**
 * @description: Java类生成器
 * @author qiancheng-su
 * @return: {@link null}
 * @date: 2022/7/5 21:20
 */
public class JavaClassBuilder {

    public AppConfig appConfig;

    public static  final  String LINE_END = "\n";

    private final JavaDefinition javaDefinition;

    public JavaClassBuilder(JavaDefinition javaDefinition){
        this.javaDefinition = javaDefinition;
    }

    public JavaClassBuilder(JavaDefinition javaDefinition,AppConfig appConfig){
        this.javaDefinition = javaDefinition;
        this.appConfig = appConfig;
    }

    /**
     * 生成class文件内容
     * @return
     */
    public HashMap.Entry<String, StringBuffer> buildJavaClass(){
        StringBuffer sb = new StringBuffer();
        buildPackage(sb);
        buildImports(sb);
        buildClassNote(sb);
        if(appConfig.isLombokEnable()){
            buildLombokAnnotation(sb);
        }
        buildClassStart(sb);
        buildFields(sb);
        if(!appConfig.isLombokEnable()){
            buildGetSets(sb);
        }
        buildCLassEnd(sb);
        // 将包名格式转化为/格式
        String fileName = javaDefinition.getPackageName().replace('.', File.separatorChar);
        fileName = fileName + File.separator + javaDefinition.getName() + ".java";
        return new HashMap.SimpleEntry<>(fileName, sb);
    }

    private void buildCLassEnd(StringBuffer sb) {
        sb.append("}").append(LINE_END);
    }

    private void buildGetSets(StringBuffer sb) {
        if(javaDefinition.getFieldMap() != null){
            for (JavaDefinition.FieldDefinition field : javaDefinition.getFieldMap().values()){
                String setKey = "set";
                String getKey = "get";
                if(field.getType() instanceof BaseDefinition && "boolean".equals(field.getType().getName())){
                    getKey = "is";
                }

                String fieldType = buildFieldType(field);
                sb.append("    public ").append(fieldType).append(" ").append(getKey)
                        .append(transKeyFirstUpper(field.getFieldName())).append("() {").append(LINE_END);
                sb.append("        return this.").append(field.getFieldName()).append(";").append(LINE_END);
                sb.append("    }").append(LINE_END).append(LINE_END);
                sb.append("    public void ").append(setKey).append(transKeyFirstUpper(field.getFieldName()))
                        .append("(").append(fieldType).append(" ").append(field.getFieldName()).append(") {").append(LINE_END);
                sb.append("        this.").append(field.getFieldName()).append(" = ")
                        .append(field.getFieldName()).append(";").append(LINE_END);
                sb.append("    }").append(LINE_END).append(LINE_END);
            }
        }
    }

    private String transKeyFirstUpper(String key){
        return key.substring(0,1).toUpperCase() + key.substring(1);
    }

    private void buildFields(StringBuffer sb) {
        if(javaDefinition.getFieldMap() != null){
            for (JavaDefinition.FieldDefinition field : javaDefinition.getFieldMap().values()){
                if(field.getAnnotations() != null){
                    for(String ann : field.getAnnotations()){
                        sb.append("    ").append(ann).append(LINE_END);
                    }
                }

                if(!StringUtils.isEmpty(field.getNote())){
                    sb.append("    ").append(field.getNote()).append(LINE_END);
                }

                String fieldType = buildFieldType(field);
                sb.append("    private ").append(fieldType).append(" ");
                sb.append(field.getFieldName()).append(";").append(LINE_END).append(LINE_END);
            }
        }
    }

    private String buildFieldType(JavaDefinition.FieldDefinition field) {
        if(field.isArray()){
            return String.format( "List<%s>", field.getType().getName());
        }

        return field.getType().getName();
    }

    private void buildClassStart(StringBuffer sb) {
        sb.append("public class ").append(javaDefinition.getName()).append(" {").append(LINE_END);
    }

    private void buildLombokAnnotation(StringBuffer sb) {
        sb.append("@Data").append(LINE_END);
    }

    private void buildImports(StringBuffer sb) {
        if(javaDefinition.getImports() != null){
            for(String importItem : javaDefinition.getImports()){
                sb.append("import ").append(importItem).append(";").append(LINE_END);
            }
        }

        sb.append(LINE_END);
    }

    private void buildClassNote(StringBuffer sb) {
        sb.append(javaDefinition.getNote()).append(LINE_END);
    }

    private void buildPackage(StringBuffer sb) {
        sb.append("package ");
        sb.append(javaDefinition.getPackageName()).append(";").append(LINE_END);
    }

}
