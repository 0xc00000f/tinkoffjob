package pages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import func.TestBase;
import func.WebDriverHelper;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


/**
 * Модель, описывающая платежи
 *
 * Created by admin on 21.02.2018.
 */
public class Payments extends TestBase {

    String region;
    String regionCheckValue;
    String regionTag;

    public String getRegionCheckValue() {
        return regionCheckValue;
    }

    public void setRegionCheckValue(String regionCheckValue) {
        this.regionCheckValue = regionCheckValue;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionTag() {
        return regionTag;
    }

    public void setRegionTag(String regionTag) {
        this.regionTag = regionTag;
    }

    public Payments() {

    }

    /**
     * Инициализация по тэгу региона
     *
     * @param regionTag
     *          тэг региона
     */
    public Payments(String regionTag) {
        HashMap<String, String> regionNameAndCheckValue = parseRegions(regionTag);
        setRegion(regionNameAndCheckValue.get("nameChoose"));

        String regionCheckValue = regionNameAndCheckValue.get("nameCheck");
        if (regionCheckValue != null) {
            setRegionCheckValue(regionCheckValue);
        } else setRegionCheckValue(getRegion());

    }

    /**
     * Получение структуры типа {название в поле выбора региона: значение; название в поле проверки региона: значение},
     * ключи: nameChoose - для выбора в поле выбора региона, nameCheck: для проверки региона (может быть null)
     *
     * @param city
     *          тэг региона
     * @return {@Hashmap}
     */
    public static HashMap<String, String> parseRegions(String city) {
        ObjectMapper mapper = new ObjectMapper();
        File from = new File("src/main/resources/regions.json");
        TypeReference<HashMap<String, Object>> typeRef
                = new TypeReference<HashMap<String, Object>>() {
        };

        HashMap<String, HashMap<String, String>> regions = null;
        try {
            regions = mapper.readValue(from, typeRef);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return regions.get(city);

    }


    /**
     * Адрес поля проверки региона
     */
    static String xpathPaymentsIn = "//span[@role='button' and @data-qa-file='PaymentsCatalogHeader']";


    /**
     * Выбор региона на странице Платежи
     *
     * @param payments
     *          объект
     * @param helper
     *          вспомогательный объект
     */
    public static void chooseRegion(Payments payments, WebDriverHelper helper) {
        helper.click(xpathPaymentsIn);
        helper.waitForPageLoaded();
        helper.click(TestBase.typicalXpath(payments.getRegion()));
        helper.waitForPageLoaded();
    }


    /**
     * Проверка региона на странице Платежи
     *
     * @param payments
     *          объект
     * @param helper
     *          вспомогательный объект
     */
    public static Boolean checkRegion(Payments payments, WebDriverHelper helper) {
        return helper.getElement(xpathPaymentsIn).getText().equals(payments.getRegionCheckValue());
    }

    /**
     * Перейти по ссылке элемента по его местоположению (номер строки)
     *
     * @param lineNumber
     *          номер строки
     * @param testBase
     *          вспомогательный объект
     */
    public static void clickDropDownElement(int lineNumber, TestBase testBase) {
        ArrayList<String> tmp = getListOfDropDownElements(testBase);
        testBase.getWebDriver().click(
                "//div[@data-qa-file='SuggestBlock']//following::div[@data-qa-file='SuggestEntry']/div[@data-qa-node='Text' and .='"
                + tmp.get(lineNumber) + "']");
    }

    /**
     * Получить список названий в выпадающем списке
     * Список включает себя только главное название компании, предоставляющей услугу
     *
     * @param testBase
     *          вспомогательный объект
     * @return {@ArrayList} of {@String}
     */
    public static ArrayList<String> getListOfDropDownElements(TestBase testBase) {

        ArrayList<String> result = getListOfStringsFromWebElements("//div[@data-qa-file='SuggestBlock']//following::div[@data-qa-file='SuggestEntry']/div[@data-qa-node='Text']", testBase);
        ArrayList<String> result2 = new ArrayList<String>();
        /**
         * Подаётся xpath, который вылавливает лишние данные, избавляемся в цикле
         * Цикл до .size()-1 чтобы не учитывать последний пункт меню "Показать все"
         */
        for (int i = 0; i < result.size() - 1; i++) {
            if (i % 2 == 0)
                result2.add(result.get(i));
        }

        return result2;
    }

}
