package com.ps.github_dash_example.scheduled;

import com.ps.github_dash_example.model.RepoOwnerNameBranch;
import com.ps.github_dash_example.services.SaveGHDataToElasticService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.ps.github_dash_example.services.SaveGHDataToElasticService.yyyymmddFormat;

@Component
@Getter
@Setter
public class GitHubRetrievalJob {

    @Value("${retrievalJob.daysToLookBack}")
    private Long daysToLookBack;

    // TODO: needs moving out either into ES itself, or some other persistence technology
    private Map<String,RepoOwnerNameBranch> repoOwnerNameBranchLookup = new HashMap<>();
    private Map<String, String> latestDatePerRepo = new HashMap<>();

    @Autowired
    private SaveGHDataToElasticService saveGHDataToElasticService;

    public List<RepoOwnerNameBranch> add(String repoOwner, String repoName, String branch) {
        String key = repoOwner + repoName;
        var existingOwnerNameBranch = repoOwnerNameBranchLookup.get(key);
        if (existingOwnerNameBranch != null) {
            var existingBranches = existingOwnerNameBranch.getBranches();
            if (!existingBranches.contains(branch)) {
                existingBranches.add(branch);
            }
        }
        else {
            RepoOwnerNameBranch repoOwnerNameBranch = new RepoOwnerNameBranch();
            repoOwnerNameBranch.setOwner(repoOwner);
            repoOwnerNameBranch.setName(repoName);
            repoOwnerNameBranch.getBranches().add(branch);
            this.repoOwnerNameBranchLookup.put(key, repoOwnerNameBranch);
        }
        return new ArrayList<>(this.repoOwnerNameBranchLookup.values());
    }

    @Scheduled(fixedDelayString = "${retrievalJob.delay}")
    public void runGitHubRetrievalJob() {
        String since = getSinceString();
        for (Map.Entry<String, RepoOwnerNameBranch> e : repoOwnerNameBranchLookup.entrySet()) {
            String repoOwnerAndName = e.getKey();
            RepoOwnerNameBranch repoOwnerNameBranch = e.getValue();
            for (String branch: repoOwnerNameBranch.getBranches()) {
                var sinceYYYYMMDD = latestDatePerRepo.getOrDefault(repoOwnerAndName + branch, since);
                String latestDateConsumed = saveGHDataToElasticService.saveGHDataToElastic(repoOwnerNameBranch.getOwner(), repoOwnerNameBranch.getName(), sinceYYYYMMDD, branch);
                latestDatePerRepo.put(repoOwnerAndName + branch, latestDateConsumed);
            }
        }
    }

    private String getSinceString() {
        var dateSince = Date.from(Instant.now().minus(Duration.of(daysToLookBack, ChronoUnit.DAYS)));
        return yyyymmddFormat.format(dateSince);
    }
}
