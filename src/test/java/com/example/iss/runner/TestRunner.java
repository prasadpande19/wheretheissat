package com.example.iss.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty",
                "html:target/cucumberReports/html",
                "json:target/cucumberReports/json"
        },
        features = "src/test/resources/cucumber.features/",
        glue = "com.example.iss",
        tags = "",
        monochrome = true)
public class TestRunner {

}
