package com.kidsworld.kidsping.global.batch;

import com.kidsworld.kidsping.domain.kid.service.KidMbtiService;
import com.kidsworld.kidsping.domain.question.service.GenreAnswerService;
import com.kidsworld.kidsping.domain.question.service.MbtiAnswerService;
import java.util.List;
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
                                    @Qualifier("deleteExpiredMbtiAnswerStep") Step deleteExpiredMbtiAnswerStep) {
        return new JobBuilder("deleteExpiredDataJob", jobRepository)
                .start(deleteExpiredKidMbtiStep)
                .next(deleteExpiredGenreAnswerStep)
                .next(deleteExpiredMbtiAnswerStep)
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
    public Tasklet deleteExpiredKidMbtiTasklet(KidMbtiService kidMbtiService) {
        return (contribution, chunkContext) -> {
            List<Long> expiredKidMbtiIds = kidMbtiService.findExpiredKidMbtiIds();
            kidMbtiService.deleteExpiredKidMbti(expiredKidMbtiIds);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet deleteExpiredGenreAnswerTasklet(GenreAnswerService genreAnswerService) {
        return (contribution, chunkContext) -> {
            List<Long> expiredGenreAnswerIds = genreAnswerService.findExpiredGenreAnswerIds();
            genreAnswerService.deleteExpiredGenreAnswer(expiredGenreAnswerIds);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet deleteExpiredMbtiAnswerTasklet(MbtiAnswerService mbtiAnswerService) {
        return (contribution, chunkContext) -> {
            List<Long> expiredMbtiAnswerIds = mbtiAnswerService.findExpiredMbtiAnswerIds();
            mbtiAnswerService.deleteExpiredMbtiAnswer(expiredMbtiAnswerIds);
            return RepeatStatus.FINISHED;
        };
    }
}