package func;

import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 21.02.2018.
 */
public class TestBase extends WebDriverHelper {
    /**
     * Адрес строки поиска
     */
    public static String xpathSearchLine = "//div[@data-qa-file='SearchSuggested']//input[@data-qa-file='Input']";


    // вспомогательный объект класса
    private WebDriverHelper webDriverHelper;

    /**
     * Получить вспомогательный объект класса
     *
     * @return {@WebDriverHelper}
     *              вспомогательный объект класса
     */
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


    /**
     * Получить xpath span-элемента, который часто встречается.. (? пока не нашел зависимость TODO)
     *
     * @param name название кликабельной ссылки
     * @return {@String}
     */
    public static String typicalXpath(String name) {
        return "//span[@data-qa-node='WrapTag' and @data-qa-file='UILink' and .='" + name + "']";
    }

    /**
     * Получить рандомную строку (англ. яз. + цифры)
     *
     * @param length длина строки
     * @return {@String}
     */
    public static String getRandomString(int length) {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString().toLowerCase();
        return saltStr;

    }

    /**
     * Получить рандомное число, состоящее из заданного количества цифр
     *
     * @param length количество цифр в числе
     * @return {@long}
     */
    public static long getRandomNumber(int length) {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        long saltStr = Long.valueOf(salt.toString().trim());
        return saltStr;
    }

    /**
     * Получить рандомный российский телефон (10 цифр, начинается с 9)
     *
     * @return {@long}
     */
    public static long getRandomRussianTelephoneNumber() {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        salt.append(9);
        while (salt.length() < 10) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return Long.valueOf(salt.toString().trim());
    }

    /**
     * Получить рандомный почтовый ящик gmail.com
     *
     * @return {@String}
     */
    public static String getTypicalGmail() {
        return getRandomString(12) + "@gmail.com";
    }

    public String getHtmlSourceCode() {
        WebElement element = getWebDriver().getElement("//body");
        return element.getText();
    }

    /**
     * Получить xpath input-поля в DOM-дереве по его имени (@name)
     *
     * @param namefield
     * @return {@String}
     */
    public static String getInputXpath(String namefield) {
        return "//input[@name='" + namefield + "']";
    }

    public static ArrayList<String> getListOfStringsFromWebElements(String pathToElements, TestBase testBase) {

        List<WebElement> elements = testBase.getWebDriver().getElements(pathToElements);
        ArrayList<String> rtrn = new ArrayList<String>();
        for (WebElement element1 : elements) {
            rtrn.add(element1.getText());
        }

        return rtrn;
    }


    /**
     * Получить первый элемент ArrayList'a
     *
     * @param list
     *          список элементов
     * @return {@String}
     */
    public static String getFirstElem(ArrayList<String> list) {
        // по-хорошему нужен дженерик
        return list.get(0);
    }

}
