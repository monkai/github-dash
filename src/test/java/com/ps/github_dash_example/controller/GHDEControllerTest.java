package com.ps.github_dash_example.controller;

import com.ps.github_dash_example.model.RepoOwnerNameBranch;
import com.ps.github_dash_example.scheduled.GitHubRetrievalJob;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(classes = { GHDEControllerTest.class})
@TestConfiguration
class GHDEControllerTest {

    @Mock GitHubRetrievalJob retrievalJob;
    @Mock Model model;


    @Bean
    public GitHubRetrievalJob getGitHubRetrievalJob() {
        return retrievalJob;
    }

    @Autowired
    GHDEController ghDashController;

    @Test
    void getGHDataByAddRepoEndpoint() {
        RepoOwnerNameBranch testRepoOwnerNameBranch = new RepoOwnerNameBranch();
        List<RepoOwnerNameBranch> list = new ArrayList<>();
        list.add(testRepoOwnerNameBranch);
        when(retrievalJob.add(eq("monkai"), eq("github-dash"), eq("master"))).thenReturn(list);
        model = mock(Model.class);

        var result = ghDashController.getAddRepoEndpoint(model, "monkai", "github-dash", "master");
        assertEquals("dashboard", result);
        verify(model).addAttribute(eq("repoOwnerNameBranchList"), eq(list));

    }
}