package com.kidsworld.kidsping.global.batch;

import com.kidsworld.kidsping.domain.kid.service.KidMbtiHistoryService;
import com.kidsworld.kidsping.domain.kid.service.KidMbtiService;
import com.kidsworld.kidsping.domain.kid.service.KidService;
import com.kidsworld.kidsping.domain.kid.service.impl.LevelBadgeService;
import com.kidsworld.kidsping.domain.question.service.GenreAnswerService;
import com.kidsworld.kidsping.domain.question.service.MbtiAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ExpiredDataCleanupJob extends DefaultBatchConfiguration {

    @Bean
    public Job deleteExpiredDataJob(JobRepository jobRepository,
                                    @Qualifier("deleteExpiredKidMbtiStep") Step deleteExpiredKidMbtiStep,
                                    @Qualifier("deleteExpiredGenreAnswerStep") Step deleteExpiredGenreAnswerStep,
                                    @Qualifier("deleteExpiredMbtiAnswerStep") Step deleteExpiredMbtiAnswerStep,
                                    @Qualifier("deleteExpiredKidMbtiHistoryStep") Step deleteExpiredKidMbtiHistoryStep,
                                    @Qualifier("deleteExpiredKidBadgeAwardedStep") Step deleteExpiredKidBadgeAwardedStep,
                                    @Qualifier("deleteExpiredKidStep") Step deleteExpiredKidStep) {
        return new JobBuilder("deleteExpiredDataJob", jobRepository)
                .start(deleteExpiredKidMbtiStep)
                .next(deleteExpiredGenreAnswerStep)
                .next(deleteExpiredMbtiAnswerStep)
                .next(deleteExpiredKidMbtiHistoryStep)
                .next(deleteExpiredKidBadgeAwardedStep)
                .next(deleteExpiredKidStep)
                .build();
    }

    @Bean
    public Step deleteExpiredKidMbtiStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                         @Qualifier("deleteExpiredKidMbtiTasklet") Tasklet deleteExpiredKidMbtiTasklet) {
        return new StepBuilder("deleteExpiredKidMbtiStep", jobRepository)
                .tasklet(deleteExpiredKidMbtiTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step deleteExpiredGenreAnswerStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                             @Qualifier("deleteExpiredGenreAnswerTasklet") Tasklet deleteExpiredGenreAnswerTasklet) {
        return new StepBuilder("deleteExpiredGenreAnswerStep", jobRepository)
                .tasklet(deleteExpiredGenreAnswerTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step deleteExpiredMbtiAnswerStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                            @Qualifier("deleteExpiredMbtiAnswerTasklet") Tasklet deleteExpiredMbtiAnswerTasklet) {
        return new StepBuilder("deleteExpiredMbtiAnswerStep", jobRepository)
                .tasklet(deleteExpiredMbtiAnswerTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step deleteExpiredKidMbtiHistoryStep(JobRepository jobRepository,
                                                PlatformTransactionManager transactionManager,
                                                @Qualifier("deleteExpiredKidMbtiHistoryTasklet") Tasklet deleteExpiredKidMbtiHistoryTasklet) {
        return new StepBuilder("deleteExpiredKidMbtiHistoryStep", jobRepository)
                .tasklet(deleteExpiredKidMbtiHistoryTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step deleteExpiredKidBadgeAwardedStep(JobRepository jobRepository,
                                                 PlatformTransactionManager transactionManager,
                                                 @Qualifier("deleteExpiredKidBadgeAwardedTasklet") Tasklet deleteExpiredKidBadgeAwardedTasklet) {
        return new StepBuilder("deleteExpiredKidBadgeAwardedStep", jobRepository)
                .tasklet(deleteExpiredKidBadgeAwardedTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step deleteExpiredKidStep(JobRepository jobRepository,
                                     PlatformTransactionManager transactionManager,
                                     @Qualifier("deleteExpiredKidTasklet") Tasklet deleteExpiredKidTasklet) {
        return new StepBuilder("deleteExpiredKidStep", jobRepository)
                .tasklet(deleteExpiredKidTasklet, transactionManager)
                .build();
    }

    @Bean
    public Tasklet deleteExpiredKidMbtiTasklet(KidMbtiService kidMbtiService) {
        return (contribution, chunkContext) -> {
            kidMbtiService.deleteExpiredKidMbti();
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet deleteExpiredGenreAnswerTasklet(GenreAnswerService genreAnswerService) {
        return (contribution, chunkContext) -> {
            genreAnswerService.deleteExpiredGenreAnswer();
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet deleteExpiredMbtiAnswerTasklet(MbtiAnswerService mbtiAnswerService) {
        return (contribution, chunkContext) -> {
            mbtiAnswerService.deleteExpiredMbtiAnswer();
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet deleteExpiredKidMbtiHistoryTasklet(KidMbtiHistoryService kidMbtiHistoryService) {
        return (contribution, chunkContext) -> {
            kidMbtiHistoryService.deleteExpiredKidMbtiHistory();
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet deleteExpiredKidBadgeAwardedTasklet(LevelBadgeService levelBadgeService) {
        return (contribution, chunkContext) -> {
            levelBadgeService.deleteExpiredKidBadgeAwarded();
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet deleteExpiredKidTasklet(KidService kidService) {
        return (contribution, chunkContext) -> {
            kidService.deleteExpiredKid();
            return RepeatStatus.FINISHED;
        };
    }
}