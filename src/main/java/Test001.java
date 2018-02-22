/**
 * Created by admin on 21.02.2018.
 */

import func.TestBase;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.Payments;
import models.payment.housing.PaymentHousingMoscowWithId;

import static pages.Payments.checkRegion;
import static pages.Payments.chooseRegion;
import static models.payment.housing.PaymentHousingMoscowWithId.xpathButtonSubmit;
import static models.payment.housing.PaymentHousingMoscowWithId.xpathKnowArrears;


/**
 * Creating with main goal to understand all necessary features for app architecture
 */

public class Test001 extends TestBase {

    @Test(description = "FirstTry")
    public void testTry() {

        getWebDriver().isElementDisplayed("//div[@data-qa-file='UIProductBlockHeader']");

        WebElement payments = getWebDriver().getElement("//span[@data-qa-file='MenuItem' and .='Платежи']");
        payments.click();
        getWebDriver().waitForPageLoaded();

        Payments payments1 = new Payments("MSK");

        // choose region for payment
        chooseRegion(payments1, getWebDriver());
        checkRegion(payments1, getWebDriver());

        // click on button to pay for housing
        getWebDriver().click(typicalXpath("ЖКХ"));
        getWebDriver().waitForPageLoaded();

        getWebDriver().click(typicalXpath("ЖКУ-Москва"));
        getWebDriver().waitForPageLoaded();

        PaymentHousingMoscowWithId housing = new PaymentHousingMoscowWithId();

        // вводим код и мыло
        getWebDriver().inputText("//input[@name='fieldpayerCode']", Long.toString(housing.getPaymentCode()));
        getWebDriver().inputText("//input[@name='email']", housing.getEmail());

        // вводим логин как телефонный номер (валидный) : проверки - пасс невидим, кнопка "Войти" активна
        getWebDriver().inputText("//input[@name='login']", Long.toString(housing.getTelephoneNumber()));
        Assert.assertTrue(!getWebDriver().isElementDisplayed("//input[@name='password']"));
        Assert.assertTrue(getWebDriver().getElement(xpathButtonSubmit).isEnabled());

        getWebDriver().clearText("//input[@name='login']");

        // вводим логин как логин, поле пароль видимо, кнопка "Узнать задолженность" неактивна
        getWebDriver().inputText("//input[@name='login']", housing.getLogin());
        getWebDriver().waitForPageLoaded();
        Assert.assertTrue(getWebDriver().isElementDisplayed("//input[@name='password']"));

        // вводим пароль, кнопка должна быть активна
        getWebDriver().inputText("//input[@name='password']", housing.getPassword());

        // очищаем все поля
        getWebDriver().clearText("//input[@name='fieldpayerCode']");
        getWebDriver().clearText("//input[@name='login']");
        getWebDriver().clearText("//input[@name='email']");

        // нажимаем кнопку "Узнать задолженность"
        getWebDriver().click(xpathKnowArrears);
        getWebDriver().waitForPageLoaded();


        Assert.assertEquals("Поле обязательное",
                getWebDriver().getElement("//input[@name='fieldpayerCode']/ancestor::div[3]//div[@data-qa-file='UIFormRowError']").getText());
        Assert.assertEquals("Поле обязательное",
                getWebDriver().getElement("//input[@name='login']/ancestor::div[3]//div[@data-qa-file='UIFormRowError']").getText());

        Assert.assertTrue(!getWebDriver().isElementDisplayed("//input[@name='password']"));


    }


}
