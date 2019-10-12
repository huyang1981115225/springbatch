package com.yusys.itemreader;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

/**
 * 数据读取
 * Created by huyang on 2019/10/9.
 */
//@Configuration
public class ItemReaderDemo {

    // 注入创建任务对象的对象
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    // 任务的执行由step决定
    // 注入创建step对象的对象
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job itemReader() {
        return jobBuilderFactory.get("itemReader")
                .start(itemReaderDemoStep())
                .build();
    }

    @Bean
    public Step itemReaderDemoStep() {
        return stepBuilderFactory.get("itemReaderDemoStep")
                .<String, String>chunk(2)
                .reader(itemReaderDemoRead())
                .writer(list -> {
                    for (String item : list) {
                        System.out.println(item + "...");
                    }
                })
                .build();
    }


    @Bean
    public MyReader itemReaderDemoRead() {
        List<String> data = Arrays.asList("cat", "dog", "pig", "duck");
        return new MyReader(data);
    }
}
