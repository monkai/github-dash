package com.ps.github_dash_example.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;
import java.util.Objects;

@Data
@NoArgsConstructor
@Document(indexName = "ghdata", createIndex = true)
public class GHData {
    @Id
    private Integer key;

    @Field(type = FieldType.Object, name = "json-data")
    private Map<String, Object> data;

    @Field(type = FieldType.Text, name = "repo")
    private String repo;

    @Field(type = FieldType.Text, name = "branch")
    private String branch;

    public GHData(String repo, String branch){
        this.repo = repo;
        this.branch = branch;
    }

    public Integer getKey() {
        return key;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {

        this.data = data;

        createKeyWithData(data);

    }

    private void createKeyWithData(Map<String, Object> data) {
        String name = null;
        String date = null;
        String repoOwnerAndName = null;
        if (data.get("commit") instanceof Map commit) {
            if(commit.get("author") instanceof Map author) {
                name = String.valueOf(author.get("name"));
                date = String.valueOf(author.get("date"));
            }
            if(commit.get("url") instanceof String url) {
                url = url.replace("https://api.github.com/repos/", "");
                int i = url.indexOf("/");
                i = url.indexOf("/", i+1);
                repoOwnerAndName = url.substring(0, i);
            }
            key = Objects.hash(repoOwnerAndName, branch, name,date); // Avoid duplicate ES documents with this compound key
        }
    }
}
