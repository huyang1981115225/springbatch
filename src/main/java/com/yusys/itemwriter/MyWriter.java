package com.yusys.itemwriter;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据输出
 * Created by huyang on 2019/10/11.
 */
@Component("myWriter")
public class MyWriter implements ItemWriter<String> {
    @Override
    public void write(List<? extends String> items) throws Exception {
        // TODO 数据是一批一批的输出
        System.out.println("Size:  " + items.size());
        for (String str : items) {
            System.out.println(str);
        }
    }
}
