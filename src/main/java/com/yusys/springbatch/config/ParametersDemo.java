package com.yusys.springbatch.config;

import listener.MyStepListener;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 演示Job参数
 *
 * Created by huyang on 2019/10/9.
 */
//@Configuration
//@EnableBatchProcessing
public class ParametersDemo implements StepExecutionListener {

    // 注入创建任务对象的对象
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    // 任务的执行由step决定
    // 注入创建step对象的对象
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    private Map<String,JobParameter> parameters;

    @Bean
    public Job parametersDemoJob(){
        return  jobBuilderFactory.get("parametersDemoJob")
                .start(parametersTep())
                .build();
    }

    /**
     * Job执行的是Step,job使用的数据肯定是在step中
     * 那么，我们只需要给step传递数据
     * 如何给step传递参数？？？
     * 可以使用监听的方式，例如使用step监听的方式
     */
    @Bean
    public Step parametersTep() {
        return  stepBuilderFactory.get("parametersTep")
                .listener(this)// TODO 可以使用监听的方式，例如使用step监听的方式
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        // 输出接收到的参数值
                        System.out.println(parameters.get("info"));
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        parameters = stepExecution.getJobParameters().getParameters();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
