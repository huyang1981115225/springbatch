package com.yusys.itemreader_db;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by huyang on 2019/10/9.
 */
@Component("dbJdbcWriter")
public class dbJdbcWriter implements ItemWriter<User> {
    @Override
    public void write(List<? extends User> items) throws Exception {
        for(User user :items){
            System.out.println(user);
        }
    }
}
