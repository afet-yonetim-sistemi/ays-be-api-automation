package com.ays.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

//Trigger the whole framework from this class

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "pretty",
                "html:target/cucumber-report.html", //html report
                "me.jvt.cucumber.report.PrettyReports:target/cucumber",
                "json:target/cucumber.json" // cucumber report
        },
        features = "src/test/resources/features",
        glue = "com/ays/stepDefinitions",
        dryRun = false,
        tags = "@wip"
)

public class CukesRunner {

}
