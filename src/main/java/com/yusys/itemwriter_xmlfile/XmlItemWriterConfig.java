package com.yusys.itemwriter_xmlfile;

import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huyang on 2019/10/11.
 */
@Configuration
public class XmlItemWriterConfig {

    @Bean
    public StaxEventItemWriter<Customer> xmlItemWriter() throws Exception {

        StaxEventItemWriter<Customer> writer = new StaxEventItemWriter<>();

        XStreamMarshaller marshaller = new XStreamMarshaller();

        Map<String, Class> aliases = new HashMap<>();
        aliases.put("customer", Customer.class);// TODO 指定标签
        marshaller.setAliases(aliases);

        writer.setRootTagName("customers");// TODO 指定根标签

        writer.setMarshaller(marshaller);

        String path = "E:\\上海\\文件整理\\customer.xml";
        writer.setResource(new FileSystemResource(path));
        writer.afterPropertiesSet()
        ;
        return writer;
    }
}
