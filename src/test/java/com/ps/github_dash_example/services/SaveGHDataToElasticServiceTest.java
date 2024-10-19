package com.ps.github_dash_example.services;

import com.ps.github_dash_example.config.Config;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


@ExtendWith({SpringExtension.class})
@SpringBootTest(classes = { Config.class})
class SaveGHDataToElasticServiceTest {

    @Autowired
    SaveGHDataToElasticService service;

    @Test
    public void stuff() throws ParseException {
        String inputString = "2024-10-17T11:33:11Z";
        SimpleDateFormat fromUser = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String reformattedStr = null;
        try {

            reformattedStr = myFormat.format(myFormat.parse(inputString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveGHDataToElasticService() {
        service.saveGHDataToElastic("monkai", "github-dash", "2024-01-01", "master");
    }

}