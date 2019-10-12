package com.yusys.itemprocessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huyang on 2019/10/11.
 */
//@Configuration
public class ItemProcessorDemo {

    // 注入创建任务对象的对象
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    // 任务的执行由step决定
    // 注入创建step对象的对象
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("dbJdbcReader")
    private ItemReader<? extends Customer> dbJdbcReader;

    @Autowired
    @Qualifier("dbFileWriter")
    private ItemWriter<? super Customer> dbFileWriter;

    @Autowired
    private ItemProcessor<Customer,Customer> nameUpperCaseProcessor;

    @Autowired
    private ItemProcessor<Customer,Customer> idFilterProcessor;

    @Bean
    public Job itemProcessorDemoJob(){
        return jobBuilderFactory.get("itemProcessorDemoJob")
                .start(itemProcessorDemoStep())
                .build();
    }

    @Bean
    public Step itemProcessorDemoStep() {
        return stepBuilderFactory.get("itemProcessorDemoStep")
                .<Customer,Customer>chunk(3)
                .reader(dbJdbcReader)// 读取数据
                // TODO 处理数据
                .processor(process())
                .writer(dbFileWriter)// 输出数据
                .build();
    }

    // TODO 如果有多种

    @Bean
    public CompositeItemProcessor<Customer,Customer> process(){
        CompositeItemProcessor<Customer,Customer> processor = new CompositeItemProcessor<>();

        List<ItemProcessor<Customer,Customer>> delegates = new ArrayList<>();
        delegates.add(nameUpperCaseProcessor);
        delegates.add(idFilterProcessor);

        processor.setDelegates(delegates);

        return processor;
    }
}
