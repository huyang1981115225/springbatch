package com.yusys.itemwriter_db;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据输出到数据库
 * Created by huyang on 2019/10/11.
 */
//@Configuration
public class ItemWriterDbDemo {

    // 注入创建任务对象的对象
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    // 任务的执行由step决定
    // 注入创建step对象的对象
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("flatFileReader")
    // TODO 和FlatFileReaderConfig 的方法名一样
    private ItemReader<? extends Customer> flatFileReader;

    @Autowired
    @Qualifier("itemWriterDb")
    private ItemWriter<? super Customer> itemWriterDb;

    @Bean
    public Job itemWriterDbDemoJob(){
        return jobBuilderFactory.get("itemWriterDbDemoJob")
                .start(itemWriterDbDemoStep())
                .build();
    }

    @Bean
    public Step itemWriterDbDemoStep() {
        return stepBuilderFactory.get("itemWriterDbDemoStep")
                .<Customer,Customer>chunk(5)
                .reader(flatFileReader)
                .writer(itemWriterDb)
                .build();
    }
}
