package models.payment.housing;

import func.TestBase;

import java.util.ArrayList;
import java.util.Random;

import static func.TestBase.getInputXpath;
import static func.TestBase.getListOfStringsFromWebElements;

/**
 * Модель описания оплату ЖКХ в г.Москве, используя код плательщика
 *
 * Created by admin on 22.02.2018.
 */
public class PaymentHousingMoscowWithId {

    long paymentCode;
    String email;
    long telephoneNumber;
    String login;
    String password;

    public static String xpathButtonSubmit = "//button[@type='submit' and @data-qa-file='UIButton']/span[.='Войти']";
    public static String xpathKnowArrears = "//button[@data-qa-file='UIButton' and .='Узнать задолженность']";

    public static String getXpathInputPaymentCode() {
        return getInputXpath("fieldpayerCode");
    }

    public static String getXpathInputEmail() {
        return getInputXpath("email");
    }

    public static String getXpathInputLogin() {
        return getInputXpath("login");
    }

    public static String getXpathInputTelephone() {
        return getInputXpath("login");
    }

    public static String getXpathInputPassword() {
        return getInputXpath("password");
    }

    public static String getXpathErrorMessage(String xpath) {
        return xpath + "/ancestor::div[3]//div[@data-qa-file='UIFormRowError']";
    }


    public long getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(long paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(long telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PaymentHousingMoscowWithId() {
        setPaymentCode(generatePaymentCode());
        setEmail(TestBase.getTypicalGmail());
        setLogin(generateRandomLogin());
        setPassword(TestBase.getRandomString(6));
        setTelephoneNumber(TestBase.getRandomRussianTelephoneNumber());
    }

    /**
     * Получить код плательщика
     *
     * @return {@long}
     */
    public static long generatePaymentCode() {
            return TestBase.getRandomNumber(10);
    }

    /**
     * Получить неподходящие данные для кода плательщика
     *
     * @return {@long}
     */
    public static long generateInvalidPaymentCode() {
        Random random = new Random();
        return TestBase.getRandomNumber(random.nextInt(5)+4);
    }

    /**
     * Получить логин плтельщика
     *
     * @return {@long}
     */
    public static String generateRandomLogin() {
        Random random = new Random();
        return TestBase.getRandomString(random.nextInt(8)+6);
    }

    public static ArrayList<String> getListOfCircleElements(TestBase testBase) {
        return getListOfStringsFromWebElements("//li[@data-qa-file='UIMenuItemProvider']/span[2]//span", testBase);
    }

}
