package com.yusys.itemprocessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * Created by huyang on 2019/10/11.
 */
@Configuration
public class DbFileWriterConfig {

    @Bean
    public FlatFileItemWriter<Customer> dbFileWriter() throws Exception {

        // TODO 把Customer对象转换成字符串输出到文件
        FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();
        String path = "E:\\上海\\文件整理\\customer.txt";
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
        writer.afterPropertiesSet();;
        return writer;
    }
}
