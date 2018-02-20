/**
 * Created by admin on 21.02.2018.
 */

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.File;

import java.lang.*;


/**
 * Creating with main goal to understand all necessary features for app architecture
 */

public class Test001 {

    @Test(description = "FirstTry")
    public static void testTry() {
        // 1. configure pom.xml

        // 2. initiate chromedriver
        // (1) Необходимо  задавать путь к драйверу через конфиги запуска теста

        String exePath = "chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", exePath);
        WebDriver driver = new ChromeDriver();

        String baseUrl = "http://tinkoff.ru";

        String expectedTitle = "Welcome: Mercury Tours";
        String actualTitle = "";

        // launch Fire fox and direct it to the Base URL
        driver.get(baseUrl);

        // (2) проверка ожидания элемента, кликабельности и пр..
        // (3) закрытие браузера
        // (4) логгер
        WebElement logo = driver.findElement(By.xpath("//div[@data-qa-file='UIProductBlockHeader']"));

        WebDriverWait wait = new WebDriverWait(driver,20);
        wait.until(ExpectedConditions.elementToBeClickable(logo));

        // Перехд на "Платежи"

        // интересный момент со скрином экрана, если фулл скрин, то кнопка видна, если нет, то необходимо прожимать меню
        // пока напишем для фулл скрина

        WebElement payments = driver.findElement(By.xpath("//span[@data-qa-file='MenuItem' and .='Платежи']"));
        payments.click();

        // Перещли на Платежи, проверка
//      TODO: Падает, обдумать
//        WebElement paymentLine = driver.findElement(By.xpath("//span[@data-qa-file='Input' and .='Название или ИНН получателя платежа']"));
//        Assert.assertTrue(paymentLine.isDisplayed());





    }

}
