package com.yusys.error;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Map;

/**
 * Created by huyang on 2019/10/11.
 */
//@Configuration
public class ErrorDemo {

    // 注入创建任务对象的对象
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    // 任务的执行由step决定
    // 注入创建step对象的对象
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job errorDemoJob(){
        return jobBuilderFactory.get("errorDemoJob")
                .start(errorStep1())
                .next(errorStep2())
                .build();
    }

    @Bean
    public Step errorStep1() {
        return stepBuilderFactory.get("errorStep1")
                .tasklet(errorHanding())
                .build();
    }

    @Bean
    public Step errorStep2() {
        return stepBuilderFactory.get("errorStep2")
                .tasklet(errorHanding())
                .build();
    }

    @Bean
    @Scope
    public Tasklet errorHanding() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                Map<String,Object> stepExecutionContext = chunkContext.getStepContext().getStepExecutionContext();
                if (stepExecutionContext.containsKey("yusys")){
                    System.out.println("The second run will success");
                    return  RepeatStatus.FINISHED;
                }else{
                    // TODO 上下文中放入键值对
                    System.out.println("The first run will fail");
                    chunkContext.getStepContext().getStepExecution().getExecutionContext().put("yusys",true);
                    throw  new RuntimeException("error...");
                }
            }
        };
    }
}
