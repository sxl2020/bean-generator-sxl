package com.sxl.beangeneratorsxl.builder;

import com.sxl.beangeneratorsxl.config.AppConfig;
import com.sxl.beangeneratorsxl.parser.definition.AbstractDefinition;
import com.sxl.beangeneratorsxl.parser.definition.JavaDefinition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


public class JavaFileOutput implements JavaOutput{

    AppConfig config;

    public JavaFileOutput(AppConfig config){
        this.config = config;
    }

    public void print(JavaDefinition root) throws Exception{
        Map<String, StringBuffer> javaClassContent = new HashMap<String, StringBuffer>();
        // 构建类文件
        buildClasses(root, javaClassContent);
        for (String path: javaClassContent.keySet()){
            String fileAbsPath = getAbsPath(path);
            File file = new File(fileAbsPath);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }

            if(!file.exists()){
                file.createNewFile();
                file.setWritable(true);
            }

            writeClassToFile(file, javaClassContent.get(path));
            System.out.println(file.getAbsolutePath());
        }
    }

    private void writeClassToFile(File file, StringBuffer classContent) throws Exception{
        OutputStream os = new FileOutputStream(file);
        os.write(classContent.toString().getBytes("utf-8"));
        os.close();
    }

    private String getAbsPath(String path) {
        String ppath = null;
        if(config.getOutPath() == null){
            ppath = "";
        } else {
            ppath = config.getOutPath();
        }

        if(!ppath.endsWith(File.separator)){
            ppath = ppath + File.separator;
        }

        return ppath + path;
    }

    protected void buildClasses(JavaDefinition javaDefinition, Map<String, StringBuffer> javaClassContent){
        JavaClassBuilder builder = new JavaClassBuilder(javaDefinition,config);
        // 创建Java类，并写入到
        Map.Entry<String, StringBuffer> entry = builder.buildJavaClass();
        javaClassContent.put(entry.getKey(), entry.getValue());
        Map<String, JavaDefinition.FieldDefinition> fields = javaDefinition.getFieldMap();
        for (JavaDefinition.FieldDefinition definition : fields.values()){
            AbstractDefinition fieldType = definition.getType();
            if(fieldType instanceof  JavaDefinition){
                buildClasses((JavaDefinition)fieldType, javaClassContent);
            }
        }
    }
}
