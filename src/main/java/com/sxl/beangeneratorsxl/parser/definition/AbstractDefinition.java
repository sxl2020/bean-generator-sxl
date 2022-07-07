package com.sxl.beangeneratorsxl.parser.definition;

import lombok.Data;

/**
 * @author qiancheng-su
 * @description: 属性及该属性依赖的包名
 * @date 2022/7/5 13:32
 */
@Data
public abstract class AbstractDefinition {
    protected String packageName;
    protected String name;
}
