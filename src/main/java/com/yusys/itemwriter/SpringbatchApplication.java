package com.yusys.itemwriter;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 演示读取数据
 */
@SpringBootApplication
@EnableBatchProcessing // 这里加了就不用后面一个个加了
public class SpringbatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbatchApplication.class, args);
    }

}
