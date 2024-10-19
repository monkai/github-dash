package com.ps.github_dash_example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = { "com.ps.github_dash_example.*" })
public class Config extends ElasticsearchConfiguration {

    @Value("${elasticsearch.pwd}")
    private String pwd;
    @Value("${elasticsearch.user}")
    private String user;

    @Override
    public ClientConfiguration clientConfiguration() {

        return ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .withBasicAuth("elastic", "ZDBqeQnD")
                .withBasicAuth(user, pwd)
                .build();
    }

}

