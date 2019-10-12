package com.yusys.itemreader_file;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by huyang on 2019/10/9.
 */
@Component("flatFileWrite")
public class FlatFileWrite implements ItemWriter<Customer> {
    @Override
    public void write(List<? extends Customer> items) throws Exception {
        for(Customer customer :items){
            System.out.println(customer);
        }
    }
}
