package com.yusys.springbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.JobStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.job.JobStep;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Created by huyang on 2019/10/9.
 */
//@Configuration
//@EnableBatchProcessing
public class ParentJob {

    // 注入创建任务对象的对象
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    // 任务的执行由step决定
    // 注入创建step对象的对象
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private Job childJobOne;// ChildJob1中@Bean

    @Autowired
    private Job childJobTwo;// ChildJob2中@Bean

    // 启动器对象
    @Autowired
    private JobLauncher launcher;

    @Bean
    public Job parentJobs(JobRepository repository, PlatformTransactionManager transactionManager) {
        return jobBuilderFactory.get("parentJobs")
                .start(parentChildJob1(repository,transactionManager))
                .next(parentChildJob2(repository,transactionManager))
                .build();
    }

    // TODO 返回的是Job类型的Step,特殊的Step
    @Bean
    public Step parentChildJob1(JobRepository repository, PlatformTransactionManager transactionManager) {
        return new JobStepBuilder(new StepBuilder("parentChildJob1"))
                .job(childJobOne)
                .launcher(launcher)  //使用启动父Job的启动对象
                .repository(repository)
                .transactionManager(transactionManager).build();

    }

    // 返回的是Job类型的Step,特殊的Step
    @Bean
    public Step parentChildJob2(JobRepository repository, PlatformTransactionManager transactionManager) {
        return new JobStepBuilder(new StepBuilder("parentChildJob2"))
                .job(childJobTwo)
                .launcher(launcher)  //使用启动父Job的启动对象
                .repository(repository)
                .transactionManager(transactionManager).build();
    }
}
