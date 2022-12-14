/*
 * ${GEMATIK_COPYRIGHT_STATEMENT}
 */

package net.kal.cute.bdd;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    features = "src/test/resources/features",
    plugin = {"pretty", "html:target/site/CucumberReport.html"},
    glue = {"net.kal.cute.bdd.steps"})
public class CompleteTestSuite {}
