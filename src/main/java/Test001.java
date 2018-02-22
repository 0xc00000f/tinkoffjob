/**
 * Created by admin on 21.02.2018.
 */

import func.TestBase;
import models.payment.housing.PaymentHousingMoscowWithId;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.Payments;

import java.util.ArrayList;
import java.util.List;

import static models.payment.housing.PaymentHousingMoscowWithId.*;
import static pages.Payments.checkRegion;
import static pages.Payments.chooseRegion;


/**
 * Creating with main goal to understand all necessary features for app architecture
 */

public class Test001 extends TestBase {

    @Test(description = "FirstTry")
    public void testTry() {

        getWebDriver().isElementDisplayed("//span[@data-qa-file='TinkoffLogo']");

        WebElement payments = getWebDriver().getElement("//span[@data-qa-file='MenuItem' and .='Платежи']");
        payments.click();
        getWebDriver().waitForPageLoaded();

        Payments payments1 = new Payments("MSK");

        // choose region for payment
        chooseRegion(payments1, getWebDriver());
        checkRegion(payments1, getWebDriver());

        /**
         * Необходимость выбора первого элемента из списка, пока хардкод
         */

        // click on button to pay for housing
        getWebDriver().click(typicalXpath("ЖКХ"));
        getWebDriver().waitForPageLoaded();

        getWebDriver().click(typicalXpath("ЖКУ-Москва"));
        getWebDriver().waitForPageLoaded();

        PaymentHousingMoscowWithId housing = new PaymentHousingMoscowWithId();

        // вводим код и мыло
        getWebDriver().inputText("//input[@name='fieldpayerCode']", Long.toString(housing.getPaymentCode()));
        getWebDriver().inputText("//input[@name='email']", housing.getEmail());

        // заполним поле "телефон" некорректным значением
        getWebDriver().inputText("//input[@name='login']", Long.toString(getRandomRussianTelephoneNumber()).substring(0, 8));
        // нажимаем кнопку "Войти"
        getWebDriver().click(xpathButtonSubmit);
        // проверка выдачи правильной ошибки на веб-странице
        Assert.assertEquals("Минимальное количество символов – 10",
                getWebDriver().getElement("//input[@name='login']/ancestor::div[3]//div[@data-qa-file='UIFormRowError']").getText());


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

        // заполним поле Код плательщика некорректным значением
        getWebDriver().inputText("//input[@name='fieldpayerCode']", Long.toString(generateInvalidPaymentCode()));
        // нажимаем кнопку "Узнать задолженность"
        getWebDriver().click(xpathKnowArrears);
        // проверка выдачи правильной ошибки на веб-странице
        Assert.assertEquals("Поле неправильно заполнено",
                getWebDriver().getElement("//input[@name='fieldpayerCode']/ancestor::div[3]//div[@data-qa-file='UIFormRowError']").getText());

        // заполним поле email некорректным значением
        getWebDriver().inputText("//input[@name='email']", getRandomString(7));
        // нажимаем кнопку "Узнать задолженность"
        getWebDriver().click(xpathKnowArrears);
        // проверка выдачи правильной ошибки на веб-странице
        Assert.assertEquals("Введите корректный адрес эл. почты",
                getWebDriver().getElement("//input[@name='email']/ancestor::div[3]//div[@data-qa-file='UIFormRowError']").getText());

        /**
         * Сделали все проверки над страницей (проделали кейс до п.7)
         */

        getWebDriver().click("//span[@data-qa-file='TinkoffLogo']");

        // copypaste

        getWebDriver().click("//span[@data-qa-file='MenuItem' and .='Платежи']");
        getWebDriver().waitForPageLoaded();

        Payments payments2 = new Payments("MSK");

        // choose region for payment
        checkRegion(payments2, getWebDriver());
        getWebDriver().inputText(xpathSearchLine, "ЖКХ");
        getWebDriver().waitForPageLoaded();

        List<WebElement> list = getWebDriver().getElements(
                "//div[@data-qa-file='SuggestBlock']//following::div[@data-qa-file='SuggestEntry']/div[@data-qa-node='Text']");
        ArrayList<String> result = new ArrayList<String>(list.size());
        for (WebElement webElement : list) {
            result.add((webElement).getText());
        }

        ArrayList<String> result2 = new ArrayList<String>();
        /**
         * Подаётся xpath, который вылавливает лишние данные, избавляемся в цикле
         * Цикл до .size()-1 чтобы не учитывать последний пункт меню "Показать все"
         */
        for (int i = 0; i < result.size() - 1; i++) {
            if (i % 2 == 0)
                result2.add(result.get(i));
        }

        getWebDriver().click("//div[@data-qa-file='SuggestBlock']//following::div[@data-qa-file='SuggestEntry']/div[@data-qa-node='Text' and .='" + result2.get(0) + "']");


        /**
         * Все, что ниже, для брейкпоинтов, после удалить
         */
        Assert.assertTrue(!getWebDriver().isElementDisplayed("//input[@name='password']"));


    }


}
