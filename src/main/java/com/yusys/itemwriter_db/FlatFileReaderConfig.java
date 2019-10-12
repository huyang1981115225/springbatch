package com.yusys.itemwriter_db;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;

/**
 * Created by huyang on 2019/10/11.
 */
@Configuration
public class FlatFileReaderConfig {
    @Bean
    // TODO 方法名和ItemWriterDbDemo 注入的名字一样
    public FlatFileItemReader<? extends Customer> flatFileReader() {
        // TODO 从普通文件读取数据
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("customer.txt"));// 指定文件路径
        reader.setLinesToSkip(1);//跳过几行，可能是名称不是数据

        // TODO 解析数据
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("id","name","age");// 指明表头

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
        return  reader;
    }
}
