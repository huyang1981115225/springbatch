package com.yusys.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * Created by huyang on 2019/10/9.
 */
//@Configuration
//@EnableBatchProcessing
public class SplitDemo {

    // 注入创建任务对象的对象
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    // 任务的执行由step决定
    // 注入创建step对象的对象
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    // 创建任务
    @Bean
    public Job splitDemoJob() {
        return jobBuilderFactory.get("splitDemoJob")
                .start(splitDemoFlow1())
                // TODO Flow1 和 Flow2 并发执行
                .split(new SimpleAsyncTaskExecutor()).add(splitDemoFlow2())
                .end()
                .build();
    }

    @Bean
    public Step splitDemoStep1() {
        return stepBuilderFactory.get("splitDemo-step1")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("Hello Spring batch,I'm splitDemo-step1!");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step splitDemoStep2() {
        return stepBuilderFactory.get("splitDemo-step2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("Hello Spring batch,I'm splitDemo-step2!");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step splitDemoStep3() {
        return stepBuilderFactory.get("splitDemo-step3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("Hello Spring batch,I'm splitDemo-step3!");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    // 创建Flow对象　指明Flow对象包含哪些Step
    @Bean
    public Flow splitDemoFlow1() {
        return new FlowBuilder<Flow>("splitDemoFlow1")
                .start(splitDemoStep1())
                .build();
    }

    // 创建Flow对象　指明Flow对象包含哪些Step
    @Bean
    public Flow splitDemoFlow2() {
        return new FlowBuilder<Flow>("splitDemoFlow2")
                .start(splitDemoStep2())
                .next(splitDemoStep3())
                .build();
    }
}
