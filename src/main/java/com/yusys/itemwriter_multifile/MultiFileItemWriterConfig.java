package com.yusys.itemwriter_multifile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据输出到多个文件
 * Created by huyang on 2019/10/11.
 */
//@Configuration
public class MultiFileItemWriterConfig {

    /**
     * TODO 数据输出到多个文件(不分类)
     */
//    @Bean
//    public CompositeItemWriter<Customer> multiFileItemWriter() throws Exception {
//
//        CompositeItemWriter<Customer> writer = new CompositeItemWriter<>();
//
//        // TODO 指定输出对象
//        writer.setDelegates(Arrays.asList(fileItemWriter(),xmlItemWriter()));
//
//        writer.afterPropertiesSet();;
//        return writer;
//    }

    /**
     * TODO 数据输出到多个文件(分类)
     * TODO 假设 age<=30的输出到普通文件，其余的输出到xml文件
     */
    @Bean
    public ClassifierCompositeItemWriter<Customer> multiFileItemWriter() throws Exception {

        ClassifierCompositeItemWriter<Customer> writer = new ClassifierCompositeItemWriter<>();

        writer.setClassifier(new Classifier<Customer, ItemWriter<? super Customer>>() {
            @Override
            public ItemWriter<? super Customer> classify(Customer customer) {
                // TODO 按照年龄进行分类
                ItemWriter<Customer> write = null;
                try {
                    write= customer.getAge() <= 30 ? fileItemWriter():xmlItemWriter();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return write;
            }
        });

        return writer;

    }

    /**
     * 数据输出到普通文件
     */
    @Bean
    public FlatFileItemWriter<Customer> fileItemWriter() throws Exception {

        // TODO 把Customer对象转换成字符串输出到文件
        FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();
        String path = "E:\\上海\\文件整理\\customer_multiFile.txt";
        writer.setResource(new FileSystemResource(path));

        // TODO 把Customer对象转换成字符串
        writer.setLineAggregator(new LineAggregator<Customer>() {

            ObjectMapper mapper = new ObjectMapper();

            @Override
            public String aggregate(Customer item) {

                String str = null;
                try {
                    str = mapper.writeValueAsString(item);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return str;
            }
        });

        writer.afterPropertiesSet();
        return writer;
    }

    /**
     * 数据输出到XML文件
     */
    @Bean
    public StaxEventItemWriter<Customer> xmlItemWriter() throws Exception {

        StaxEventItemWriter<Customer> writer = new StaxEventItemWriter<>();

        XStreamMarshaller marshaller = new XStreamMarshaller();

        Map<String, Class> aliases = new HashMap<>();
        aliases.put("customer", Customer.class);// TODO 指定标签
        marshaller.setAliases(aliases);

        writer.setRootTagName("customers");// TODO 指定根标签

        writer.setMarshaller(marshaller);

        String path = "E:\\上海\\文件整理\\customer_multiFile.xml";
        writer.setResource(new FileSystemResource(path));
        writer.afterPropertiesSet();
        return writer;
    }
}
