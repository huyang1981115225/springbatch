package com.yusys.itemreader_xmlfile;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

/**
 * 演示从Xml文件读取数据
 * Created by huyang on 2019/10/9.
 */
//@Configuration
public class ItemReaderXmlFileDemo {

    // 注入创建任务对象的对象
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    // 任务的执行由step决定
    // 注入创建step对象的对象
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("xmlFileWriter")
    private ItemWriter<? super Customer> xmlFileWriter;

    @Bean
    public Job itemReaderXmlFileDemoJob() {
        return jobBuilderFactory.get("itemReaderXmlFileDemoJob")
                .start(itemReaderXmlFileDemoStep())
                .build();
    }

    @Bean
    public Step itemReaderXmlFileDemoStep() {
        return stepBuilderFactory.get("itemReaderXmlFileDemoStep")
                .<Customer,Customer>chunk(3)
                .reader(xmlFileReader())
                .writer(xmlFileWriter)
                .build();
    }

    @Bean
    @StepScope
    public StaxEventItemReader<? extends Customer> xmlFileReader() {
        StaxEventItemReader<Customer> reader = new StaxEventItemReader<>();
        reader.setResource(new ClassPathResource("customer.xml"));// 指定文件路径

        // TODO 指定需要处理的根标签
        reader.setFragmentRootElementName("customer");

        // TODO 把xml读取到的数据转换对象
        XStreamMarshaller unmarshaller = new XStreamMarshaller();
        Map<String,Class> map = new HashMap<>();
        map.put("customer",Customer.class);
        unmarshaller.setAliases(map);

        reader.setUnmarshaller(unmarshaller);
        return reader;
    }
}
