package func;

import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.Random;

/**
 * Created by admin on 21.02.2018.
 */
public class TestBase {


    private WebDriverHelper webDriverHelper;
    public WebDriverHelper getWebDriver() {
        if (webDriverHelper == null) {
            webDriverHelper = new WebDriverHelper();
        }
        return webDriverHelper;
    }

    @BeforeSuite(groups = "init", alwaysRun = true)
    protected void beforeSuite() {

        getWebDriver().startChromeDriverService();
        getWebDriver().initDriver();
       // Reporter.clear();
        getWebDriver().getDriver().get("http://tinkoff.ru");
    }

    @AfterSuite(alwaysRun = true)
    protected void afterSuite() {
        try {
            // stop service
            getWebDriver().stopChromeDriverService();
            // stop driver
            getWebDriver().quit();
        } catch (Exception ex) {
        }
    }

    public String getRandomString(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    public String getTypicalGmail() {
        return getRandomString(12) + "@gmail.com";
    }


}
