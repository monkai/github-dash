package com.ps.github_dash_example.controller;

import com.ps.github_dash_example.scheduled.GitHubRetrievalJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GHDEController {

    @Autowired
    GitHubRetrievalJob gitHubRetrievalJob;

    public GHDEController() {}

    @GetMapping("/dashboard")
    public String getDashboardEndpoint() {
        return "dashboard";
    }


    @PostMapping("/add-repo")
    public String getAddRepoEndpoint(Model model,
                                     @RequestParam("repoName") String repoName,
                                     @RequestParam("repoOwner") String repoOwner,
                                     @RequestParam("branch") String branch) {
        var repoNameAndOwnerList = gitHubRetrievalJob.add(repoOwner, repoName, branch);
        model.addAttribute("repoOwnerNameBranchList", repoNameAndOwnerList);
        gitHubRetrievalJob.runGitHubRetrievalJob();
        return "dashboard";
    }
}
