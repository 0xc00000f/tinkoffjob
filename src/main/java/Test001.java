/**
 * Created by admin on 21.02.2018.
 */

import func.TestBase;
import models.payment.housing.PaymentHousingMoscowWithId;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.Payments;

import java.util.ArrayList;

import static models.payment.housing.PaymentHousingMoscowWithId.*;
import static pages.Payments.*;


/**
 * Creating with main goal to understand all necessary features for app architecture
 */

public class Test001 extends TestBase {

    static String xpathPaymentsClickButton = "//span[@data-qa-file='MenuItem' and .='Платежи']";

    @Test(description = "FirstTry")
    public void testTry() {

        getWebDriver().isElementDisplayed("//span[@data-qa-file='TinkoffLogo']");
        getWebDriver().click(xpathPaymentsClickButton);
        getWebDriver().waitForPageLoaded();

        Payments payments1 = new Payments("MSK");
        // choose region for payment
        if (!checkRegion(payments1, getWebDriver()))
            chooseRegion(payments1, getWebDriver());

        /**
         * Необходимость выбора первого элемента из списка, пока хардкод
         */

        // click on button to pay for housing
        getWebDriver().click(typicalXpath("ЖКХ"));
        getWebDriver().waitForPageLoaded();

        // заходим в первый элемент, сохраняем в переменной для проверки
        String firstMoscowPaymentHousing = getFirstElem(getListOfCircleElements(this));
        getWebDriver().click(typicalXpath(firstMoscowPaymentHousing));

        getWebDriver().waitForPageLoaded();

        getWebDriver().sleep(1000);

        String pagePaymentMoscowHousing1 = getHtmlSourceCode();
        PaymentHousingMoscowWithId housing = new PaymentHousingMoscowWithId();

        // вводим код и мыло
        getWebDriver().inputText(getXpathInputPaymentCode(), Long.toString(housing.getPaymentCode()));
        getWebDriver().inputText(getXpathInputEmail(), housing.getEmail());

        // заполним поле "телефон" некорректным значением
        getWebDriver().inputText(getXpathInputTelephone(), Long.toString(getRandomRussianTelephoneNumber()).substring(0, 8));
        // нажимаем кнопку "Войти"
        getWebDriver().click(xpathButtonSubmit);
        // проверка выдачи правильной ошибки на веб-странице
        Assert.assertEquals("Минимальное количество символов – 10",
                getWebDriver().getElement(getXpathErrorMessage(getXpathInputTelephone())).getText());

        // вводим логин как телефонный номер (валидный) : проверки - пасс невидим, кнопка "Войти" активна
        getWebDriver().inputText(getXpathInputTelephone(), Long.toString(housing.getTelephoneNumber()));
        Assert.assertTrue(!getWebDriver().isElementDisplayed(getXpathInputPassword()));
        Assert.assertTrue(getWebDriver().getElement(xpathButtonSubmit).isEnabled());

        // вводим логин как логин, поле пароль видимо, кнопка "Узнать задолженность" неактивна
        getWebDriver().inputText(getXpathInputLogin(), housing.getLogin());
        getWebDriver().waitForPageLoaded();
        Assert.assertTrue(getWebDriver().isElementDisplayed(getXpathInputPassword()));

        // вводим пароль, кнопка должна быть активна
        getWebDriver().inputText(getXpathInputPassword(), housing.getPassword());

        // заполним поле Код плательщика некорректным значением
        getWebDriver().inputText(getXpathInputPaymentCode(), Long.toString(generateInvalidPaymentCode()));
        // нажимаем кнопку "Узнать задолженность"
        getWebDriver().click(xpathKnowArrears);
        // проверка выдачи правильной ошибки на веб-странице
        Assert.assertEquals("Поле неправильно заполнено",
                getWebDriver().getElement(getXpathErrorMessage(getXpathInputPaymentCode())).getText());
        // заполним поле email некорректным значением
        getWebDriver().inputText(getXpathInputEmail(), getRandomString(7));
        // нажимаем кнопку "Узнать задолженность"
        getWebDriver().click(xpathKnowArrears);
        // проверка выдачи правильной ошибки на веб-странице
        Assert.assertEquals("Введите корректный адрес эл. почты",
                getWebDriver().getElement(getXpathErrorMessage(getXpathInputEmail())).getText());

        /**
         * Сделали все проверки над страницей (проделали кейс до п.7)
         */
        goToStartPage(getWebDriver());
        getWebDriver().click(xpathPaymentsClickButton);
        getWebDriver().waitForPageLoaded();

        Payments payments2 = new Payments("MSK");

        // choose region for payment
        checkRegion(payments2, getWebDriver());
        getWebDriver().inputText(xpathSearchLine, "ЖКХ");
        getWebDriver().waitForPageLoaded();

        clickDropDownElement(0, this);
        getWebDriver().waitForPageLoaded();
        getWebDriver().sleep(1000);


        String pagePaymentMoscowHousing2 = getHtmlSourceCode();

        /**
         * На этом моменте надо сравнить два wp
         */

        Assert.assertEquals(pagePaymentMoscowHousing1, pagePaymentMoscowHousing2);

        goToStartPage(getWebDriver());

        getWebDriver().getElement(xpathPaymentsClickButton).click();
        getWebDriver().waitForPageLoaded();

        Payments payments3 = new Payments("SPB");

        if (!checkRegion(payments3, getWebDriver()))
            chooseRegion(payments3, getWebDriver());

        getWebDriver().click(typicalXpath("ЖКХ"));
        getWebDriver().waitForPageLoaded();

        getWebDriver().sleep(1000);

        ArrayList<String> listOfNamesSpbHousing = getListOfCircleElements(this);

        Assert.assertNotEquals(getFirstElem(listOfNamesSpbHousing), firstMoscowPaymentHousing);

    }


}
