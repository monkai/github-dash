package com.ps.github_dash_example.model;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class GHDataTest {


    @Test
    void setData() {

        GHData data = new GHData("repo1", "branch1");

        Map<String, Object> m = new HashMap<>();
        Map<String, Object> commitMap = new HashMap<>();
        Map<String, Object> authorMap = new HashMap<>();

        String nameString = "name1";
        String dateString = "date1";
        String urlString = "https://api.github.com/repos/repoOwner1/repoName1/somethingelse";
        String repoOwnerAndName = null;

        m.put("commit", commitMap) ;
          commitMap.put("author", authorMap);
            authorMap.put("name", nameString);
            authorMap.put("date", dateString);
          commitMap.put("url", urlString);

          data.setData(m);

        assertEquals(Objects.hash("repoOwner1/repoName1", "branch1", "name1","date1"), data.getKey());

    }
}