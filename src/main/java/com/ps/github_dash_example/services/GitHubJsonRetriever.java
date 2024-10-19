package com.ps.github_dash_example.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ps.github_dash_example.model.GHData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GitHubJsonRetriever {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final RestClient REST_CLIENT = RestClient.builder().baseUrl("https://api.github.com").build();

    public List<GHData> getGHDataList(Object rootNode, String repoOwnerAndName, String branch, String sinceYYYYMMDD, String untilYYYYMMDD, Integer pageSize, String acceptValue, int page) {


        var ghDataList = new ArrayList<GHData>();

        try {
            StringBuilder result = new StringBuilder("{ \"" + rootNode + "\" : ");

            String url = "/repos/" + repoOwnerAndName + "/commits?branch=" + branch + "&since=" + sinceYYYYMMDD + "&until=" + untilYYYYMMDD + "&per_page=" + pageSize + "&page=" + page;
            result.append(REST_CLIENT
                    .get()
                    .uri(url)
                    .header("Accept", acceptValue)
                    .retrieve()
                    .body(String.class));
            result.append(" }");

                var map = OBJECT_MAPPER.readValue(result.toString(), Map.class);
                if (map.get(rootNode) instanceof List<?> list) {
                    System.out.println(url + "\nNumber of commits: " + list.size());
                    for (Object o : list) {
                        if (o instanceof Map<?,?> m
                        && !m.keySet().isEmpty()
                        && m.keySet().iterator().next() instanceof String) {
                            GHData ghData = new GHData(repoOwnerAndName, branch);
                            //noinspection unchecked
                            ghData.setData((Map<String, Object>) m); // safe cast - checked in the if statement above
                            ghDataList.add(ghData);
                        }
                    }
                }
        } catch (RestClientException | JsonProcessingException e) {
            e.printStackTrace(); // TODO: better logging required, based on production requirements
        }

        return ghDataList;
    }
}
