package com.ps.github_dash_example;

import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.ps.github_dash_example.repository")
@ComponentScan(basePackages = { "com.ps.github_dash_example.services" })
public class TestConfig extends ElasticsearchConfiguration {

    @Override
    public ClientConfiguration clientConfiguration() {

        return ClientConfiguration.builder()
                .connectedTo("localhost:5601")
                .build();
    }

}