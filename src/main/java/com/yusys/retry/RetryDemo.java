package com.yusys.retry;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Created by huyang on 2019/10/12.
 */
//@Configuration
public class RetryDemo {

    // 注入创建任务对象的对象
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    // 任务的执行由step决定
    // 注入创建step对象的对象
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ItemProcessor<? super String,? extends String> retryItemProcessor;

    @Autowired
    private ItemWriter<? super String> retryItemWriter;

    @Bean
    public Job retryDemoJob(){
        return jobBuilderFactory.get("retryDemoJob")
                .start(retryDemoStep())
                .build();
    }

    @Bean
    public Step retryDemoStep() {
        return stepBuilderFactory.get("retryDemoStep")
                .<String,String>chunk(3)
                .reader(reader())
                .processor(retryItemProcessor)
                .writer(retryItemWriter)
                // TODO 错误重试
                .faultTolerant()//容错
                .retry(CustomRetryException.class)// TODO 发生什么异常时重试
                .retryLimit(4)// 重试多少次
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
