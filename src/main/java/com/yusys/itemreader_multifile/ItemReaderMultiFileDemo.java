package com.yusys.itemreader_multifile;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.validation.BindException;

/**
 * 从多个文件读取数据
 * Created by huyang on 2019/10/11.
 */
//@Configuration
public class ItemReaderMultiFileDemo {

    // 注入创建任务对象的对象
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    // 任务的执行由step决定
    // 注入创建step对象的对象
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Value("classpath:/file*.txt")
    private Resource[] fileResources;
    @Autowired
    @Qualifier("multiFileWriter")
    private ItemWriter<? super Customer> multiFileWriter;

    @Bean
    public Job itemReaderMultiFileDemoJob() {
        return jobBuilderFactory.get("itemReaderMultiFileDemoJob")
                .start(itemReaderMultiFileDemoStep())
                .build();
    }

    @Bean
    public Step itemReaderMultiFileDemoStep() {

        return stepBuilderFactory.get("itemReaderMultiFileDemoStep")
                .<Customer, Customer>chunk(3)
                .reader(multiFileReader())
                .writer(multiFileWriter)
                .build();

    }

    // 从多个文件读取数据
    @Bean
    public MultiResourceItemReader<? extends Customer> multiFileReader() {
        MultiResourceItemReader<Customer> reader = new MultiResourceItemReader<>();

        reader.setDelegate(flatFileRead());
        reader.setResources(fileResources);
        return reader;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Customer> flatFileRead() {
        // TODO 从普通文件读取数据
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("customer.txt"));// 指定文件路径
//        reader.setLinesToSkip(1);//跳过几行，可能是名称不是数据

        // TODO 解析数据
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("id", "name", "age");// 指明表头

        // TODO 把解析出来的每一行数据映射为对象
        DefaultLineMapper<Customer> mapper = new DefaultLineMapper<>();
        mapper.setLineTokenizer(tokenizer);
        mapper.setFieldSetMapper(new FieldSetMapper<Customer>() {
            @Override
            public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
                Customer customer = new Customer();
                customer.setId(fieldSet.readInt("id"));
                customer.setName(fieldSet.readString("name"));
                customer.setAge(fieldSet.readInt("age"));
                return customer;
            }
        });

        mapper.afterPropertiesSet();// 做一下检查
        reader.setLineMapper(mapper);
        return reader;
    }
}
