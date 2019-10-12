package com.yusys.skip;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Created by huyang on 2019/10/12.
 */
//@Configuration
public class SkipDemo {

    // 注入创建任务对象的对象
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    // 任务的执行由step决定
    // 注入创建step对象的对象
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ItemProcessor<? super String,? extends String> skipItemProcessor;

    @Autowired
    private ItemWriter<? super String> skipItemWriter;

    @Bean
    public Job skipDemoJob(){
        return jobBuilderFactory.get("skipDemoJob")
                .start(skipDemoStep())
                .build();
    }

    @Bean
    public Step skipDemoStep() {
        return stepBuilderFactory.get("skipDemoStep")
                .<String,String>chunk(3)
                .reader(reader())
                .processor(skipItemProcessor)
                .writer(skipItemWriter)
                // TODO 错误跳过
                .faultTolerant()//容错
                .skip(CustomSkipException.class)// 发生什么异常跳过
                .skipLimit(4)
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<String> reader() {
        List<String> data = Arrays.asList("1","2","3","4","5","6","7","8");
        ListItemReader<String> reader = new ListItemReader<>(data);
        return reader;
    }
}
