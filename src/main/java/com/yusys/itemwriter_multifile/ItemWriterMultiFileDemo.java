package com.yusys.itemwriter_multifile;

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
 * Created by huyang on 2019/10/11.
 */
//@Configuration
public class ItemWriterMultiFileDemo {

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
    @Qualifier("multiFileItemWriter")
    private ItemWriter<? super Customer> multiFileItemWriter;

    @Bean
    public Job ItemWriterMultiFileDemoJob(){
        return jobBuilderFactory.get("itemWriterMultiFileDemoJob")
                .start(itemWriterMultiFileDemoStep())
                .build();
    }

    @Bean
    public Step itemWriterMultiFileDemoStep() {
        return stepBuilderFactory.get("itemWriterMultiFileDemoStep")
                .<Customer,Customer>chunk(5)
                .reader(dbJdbcReader)
                .writer(multiFileItemWriter)
                .build();
    }
}
