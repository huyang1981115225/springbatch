package com.yusys.itemprocessor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by huyang on 2019/10/11.
 */
@Component
public class NameUpperCaseProcessor implements ItemProcessor<Customer,Customer> {
    @Override
    public Customer process(Customer item) throws Exception {
        Customer customer = new Customer();
        customer.setId(item.getId());
        customer.setName(item.getName().toUpperCase());// TODO 处理数据
        customer.setAge(item.getAge());
        return customer;
    }
}
