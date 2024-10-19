package com.ps.github_dash_example.scheduled;

import com.ps.github_dash_example.services.SaveGHDataToElasticService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;


@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(classes = { GitHubRetrievalJobTest.class})
@TestConfiguration
class GitHubRetrievalJobTest {

    @Mock SaveGHDataToElasticService saveGHDataToElasticService;

    @Bean
    public SaveGHDataToElasticService getSaveGHDataToElasticService() {
        saveGHDataToElasticService = mock(SaveGHDataToElasticService.class);
        return saveGHDataToElasticService;
    };

    @Autowired
    private GitHubRetrievalJob gitHubRetrievalJob;


    @Test
    public void test() {
        gitHubRetrievalJob.add("repoOwner1","repoName1", "branch1");
        gitHubRetrievalJob.runGitHubRetrievalJob();

        // TODO: Need to investigate this further - the retrievalJob is getting hold of the real saveGHDataToElasticService Bean
        // instead of the test one.
//        Mockito.verify(saveGHDataToElasticService).saveGHDataToElastic(
//                eq("repoOwner1"),
//                eq("repoName1"),
//                any(),
//                eq("branch1"));
    }

}