package school.redrover;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import school.redrover.runner.BaseTest;
import school.redrover.runner.ProjectUtils;

import java.util.List;

import static java.sql.DriverManager.getDriver;

public class FreestyleProjectTest extends BaseTest {
    @Test
    public void testCreateNewJobArrowIconNavigatesToNewJob() {
        String expectedURL =  ProjectUtils.getBaseUrl() + "/newJob";
        String expectedTitle = "New Item [Jenkins]";

        String oldUrl = getDriver().getCurrentUrl();
        String oldTitle = getDriver().getTitle();

        getDriver().findElement(By.xpath("//a[@href='newJob']/span[@class='trailing-icon']")).click();

        String newUrl = getDriver().getCurrentUrl();
        String newTitle = getDriver().getTitle();

        Assert.assertNotEquals(newUrl, oldUrl);
        Assert.assertNotEquals(newTitle, oldTitle);
        Assert.assertEquals(newUrl, expectedURL);
        Assert.assertEquals(newTitle, expectedTitle);
    }

    @Test(dependsOnMethods = "testCreateNewJobArrowIconNavigatesToNewJob")
    public void testCreateFreestyleProject() {
        final int expectedAmountOfJobCreated = 1;
        final String expectedJobCreatedName = "Test";

        getDriver().findElement(By.xpath("//a[@href='newJob']/span[@class='trailing-icon']")).click();
        getDriver().findElement(By.id("name")).sendKeys(expectedJobCreatedName);
        getDriver().findElement(By.xpath("//li[@class='hudson_model_FreeStyleProject']")).click();
        getDriver().findElement(By.id("ok-button")).click();

        getDriver().findElement(By.id("jenkins-name-icon")).click();

        List<WebElement> jobs = getDriver().findElements(By.xpath("//table[@id='projectstatus']/tbody/tr"));

        Assert.assertEquals(jobs.size(), expectedAmountOfJobCreated);

        WebElement jobNameElement = jobs.get(0).findElement(By.xpath("//td/a/span"));
        final String actualJobeName = jobNameElement.getText();

        Assert.assertTrue(jobNameElement.isDisplayed());
        Assert.assertEquals(actualJobeName, expectedJobCreatedName);
    }
}
