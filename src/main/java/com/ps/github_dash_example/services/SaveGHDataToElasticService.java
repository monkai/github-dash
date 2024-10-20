package com.ps.github_dash_example.services;

import com.ps.github_dash_example.model.GHData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Optional;

@Component
public class SaveGHDataToElasticService {
    public static SimpleDateFormat yyyymmddFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final long DELAY_BETWEEN_GITHUB_CALLS = 5000;
    String untilYYYYMMDD = "2099-01-01"; //TODO - investigate if "until" is in fact an optional param, if so, consider omitting altogether
    Integer pageSize = 100;
    String rootNode = "data";
    String acceptValue = "application/vnd.github+json";


    @Autowired
    ElasticsearchOperations elasticsearchOperations;

    @Autowired
    GitHubJsonRetriever gitHubJsonRetriever;

    public String saveGHDataToElastic(String repoOwner, String repoName, String sinceYYYYMMDD, String branch) {
        String latestDateConsumed = null;

        int page = 1;
        Integer size = null;
        while (size == null || size.equals(pageSize)) {
            String repoOwnerAndName = repoOwner + "/" + repoName;
            var ghDataList = gitHubJsonRetriever.getGHDataList(rootNode, repoOwnerAndName, branch, sinceYYYYMMDD, untilYYYYMMDD, pageSize, acceptValue, page++);
            size = ghDataList.size();
            for (GHData ghData : ghDataList) {
                elasticsearchOperations.save(ghData);
                String date = Optional.of(ghData.getData())
                        .map(map -> map.get("commit"))
                        .map(Map.class::cast)
                        .map(map -> map.get("author"))
                        .map(Map.class::cast)
                        .map(map -> map.get("date"))
                        .map(String.class::cast)
                        .orElse("");
                try {
                    latestDateConsumed = yyyymmddFormat.format(yyyymmddFormat.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                };
            }

            try {
                Thread.sleep(DELAY_BETWEEN_GITHUB_CALLS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return latestDateConsumed;
            }
        }
        return latestDateConsumed;
    }
}
