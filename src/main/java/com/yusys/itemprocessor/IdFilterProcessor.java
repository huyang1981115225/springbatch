package com.yusys.itemprocessor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by huyang on 2019/10/11.
 */
@Component
public class IdFilterProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer item) throws Exception {
        if (item.getId() % 2 == 0) {
            return item;
        } else {
            return null;// 相当于把对象过滤掉
        }
    }
}
