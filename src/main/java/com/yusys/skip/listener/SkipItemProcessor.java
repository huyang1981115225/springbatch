package com.yusys.skip.listener;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by huyang on 2019/10/12.
 */
@Component
public class SkipItemProcessor implements ItemProcessor<String, String> {

    private int attempCount = 0;

    @Override
    public String process(String item) throws Exception {
        System.out.println("processing item:  " + item);
        if (item.equalsIgnoreCase("5")) {
            attempCount++;
            if (attempCount >= 3) {
                System.out.println("Retried " + attempCount + " times success.");
                return String.valueOf(Integer.valueOf(item) * -1);
            } else {
                System.out.println("Processor the " + attempCount + " times fail.");
                throw new CustomSkipException("Process faild,Attemp: " + attempCount);
            }
        } else {
            return String.valueOf(Integer.valueOf(item) * -1);
        }
    }
}
