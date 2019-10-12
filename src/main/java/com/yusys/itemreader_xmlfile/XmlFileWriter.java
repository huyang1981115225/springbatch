package com.yusys.itemreader_xmlfile;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by huyang on 2019/10/9.
 */
@Component("xmlFileWriter")
public class XmlFileWriter implements ItemWriter<Customer> {
    @Override
    public void write(List<? extends Customer> items) throws Exception {
        for (Customer customer : items){
            System.out.println(customer);
        }
    }
}
