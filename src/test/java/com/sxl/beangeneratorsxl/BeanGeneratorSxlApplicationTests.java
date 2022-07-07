package com.sxl.beangeneratorsxl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BeanGeneratorSxlApplicationTests {

    @Test
    public void test1(){
        String path = Thread.currentThread().getContextClassLoader().getResource("test.json").getPath();
        String[] args = new String[]{"-i", path };
        BeanGeneratorApplication.generateWithJsonFile(args);
    }

    @Test
    public void test2(){
        String path = Thread.currentThread().getContextClassLoader().getResource("InspectionAlarm.xlsx").getPath();
        String[] args = new String[]{"-i", path };
        BeanGeneratorApplication.generateWithExcelDir(args);
    }

}
