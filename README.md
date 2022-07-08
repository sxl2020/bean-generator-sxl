## 工程简介
自动生成类文件，主要包括以下两个功能：

1. 根据json文件来生成类文件
2. 根据excel文件生成类文件


## 使用说明

### 1. 进入BeanGeneratorApplication来生成代码。


- 解析excel文件来生成Java类时
需要配置：AppConfig和BeanConfig
其中AppConfig配置项如下：
```angular2
 /**
     * 输出地址: 一般项目的工程根目录
     */
    private String outPath = "";

    /**
     * json 解析框架
     */
    private String frame;

    /**
     * 是否启用lombok，默认关闭
     */
    private boolean lombokEnable;

    /**
     * 是否启用swagger，默认关闭
     */
    private boolean swaggerEnable;
```

其中BeanConfig配置项如下：
```angular2
 /**
     * 解析的文件
     */
    private File file;


    /**
     * excel表格中，表的每列对应的Java类中属性的组成部分：
     * Java类中成员的组成包括：
     * 类成员变量名称（FIELD_NAME），类成员变量数据类型（FIELD_TYPE），类成员变量注释（FIELD_NOTE），类成员变量注解（FIELD_CONSTRAINT）
     * Map中，key为上述枚举，value为excel中对应的列号。从0开始计数。
     */
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

```

配置好后，点击运行便可生成代码。

## 批量支持说明

在JavaClassGenerator中,分别支持目录和指定的文件来生成JavaBean代码。
```angular2

    void generateByExcelDir(String dirPath,  AppConfig appConfig, BeanConfig beanConfig) throws Exception;

    void generateByExcelPath(String excelPath, AppConfig appConfig, BeanConfig beanConfig) throws Exception;

    void generateByJsonDir(String dirPath, AppConfig appConfig, BeanConfig beanConfig) throws Exception;

    void generateByJsonPath(String dirPath, AppConfig appConfig, BeanConfig beanConfig) throws Exception;
```