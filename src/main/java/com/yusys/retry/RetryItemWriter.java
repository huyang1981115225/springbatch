package com.yusys.retry;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by huyang on 2019/10/12.
 */
@Component
public class RetryItemWriter implements ItemWriter<String> {
    @Override
    public void write(List<? extends String> items) throws Exception {
        for (String str : items){
            System.out.println(str);
        }
    }
}
