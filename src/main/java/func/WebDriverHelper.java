package func;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.IOException;
import java.util.List;

/**
 * Created by admin on 21.02.2018.
 */
public class WebDriverHelper  {

    /**
     * Тип используемого веб-драйвера
     */
    private transient volatile WebDriver driver;

    protected static ChromeDriverService service;
    /**
     * Максимальная задержка в секундах
     */
    public static final long TIMEOUT = 30;
    /**
     * Период опроса страницы
     */
    private static final long SLEEPINMILS = 100;

    /**
     * Запуск сервиса Chrome-драйвера
     */
    protected void startChromeDriverService() {

        if (service == null) {
            service = new ChromeDriverService.Builder().usingAnyFreePort().build();
        }

        if (!service.isRunning()) {
            // запуск сервиса
            try {
                service.start();
            } catch (IOException e) {
                // не запустился
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Завершение сервиса Chrome
     */
    protected void stopChromeDriverService() {
        if (service != null || service.isRunning()) {
            try {
                service.stop();
            } catch (Throwable e) {
            }
        }
    }

    /**
     * Закрыть веб-драйвер
     */
    public void quit() {
        try {
            synchronized (this) {
                if (driver != null) {
                    driver.quit();
                    driver = null;
                }
            }
        } catch (Throwable t) {
        }
    }

    /**
     * Инициализация Веб-драйвера
     */
    public void initDriver() {

        synchronized (this) {
            if (driver != null)
                close();

            ChromeOptions chrOptions = new ChromeOptions();
            chrOptions.addArguments("--start-maximized");

            DesiredCapabilities desiredCapabilities = DesiredCapabilities.chrome();
            desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chrOptions);

            driver = new RemoteWebDriver(service.getUrl(), desiredCapabilities);
            driver = new Augmenter().augment(driver);

        }
    }

    /**
     * Получить текущий Веб-драйвер
     *
     * @return {@link WebDriver}
     */
    public synchronized WebDriver getDriver() {
        if (driver == null)
            initDriver();
        return driver;
    }

    /**
     * Закрыть браузер
     */
    protected void close() {
        synchronized (this) {
            try {
                if (driver != null && !driver.toString().contains("null")) {
                    driver.manage().deleteAllCookies();
                    driver.close();
                    driver.quit();
                    driver = null;
                }

            } catch (Exception e) {
                if (driver != null) {
                    driver.close();
                    driver.quit();
                    driver = null;
                }
            }
        }
    }

    /**
     * Преобразовать строку в {@link By}
     *
     * @param pathToElement
     *      адрес элемента в виде строки
     * @return {@link By}
     */
    public static By getLocatorByPath(String pathToElement) {
        if (pathToElement == null)
            throw new IllegalArgumentException();
        By locator = By.xpath(pathToElement);

        return locator;
    }

    /**
     * Быстрое получение элемента
     *
     * @param pathToElement
     *          адрес элемента в виде строки
     * @return {@link WebElement}
     */
    public WebElement getElement(String pathToElement) {
        return getElement(getLocatorByPath(pathToElement));
    }

    /**
     * Быстрое получение элемента
     *
     * @param locator
     *          адрес элемента
     * @return {@link WebElement}
     */
    public WebElement getElement(By locator) {

        try {
            return getDriver().findElement(locator);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * Получение списка элементов
     *
     * @param pathToElement
     *          адрес элемента
     * @return {@link List} of {@link WebElement}
     */
    public List<WebElement> getElements(String pathToElement) {
        return getElements(getLocatorByPath(pathToElement));
    }

    /**
     * Получение списка элементов
     *
     * @param locator
     *          адрес элемента
     * @return {@link List} of {@link WebElement}
     */
    public List<WebElement> getElements(By locator) {
        return getDriver().findElements(locator);
    }


    /**
     * Проверка отображения элемента
     *
     * @param pathToElement
     *              адрес элемента
     * @return true - элемент отображается на странице
     *         false - элемент не отображается на странице
     */
    public boolean isElementDisplayed(String pathToElement) {
        return isElementDisplayed(getLocatorByPath(pathToElement));
    }

    /**
     * Проверка отображения элемента
     *
     * @param locator
     *              адрес элемента
     * @return true - элемент отображается на странице
     *         false - элемент не отображается на странице
     */
    public boolean isElementDisplayed(By locator) {
        // элемент не виден по-умолчанию
        boolean result = false;
        try {
            WebElement element = getElement(locator);
            result = element != null && element.isDisplayed();
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    /**
     * Клик c ожиданием на элемент html-страницы
     *
     * @param pathToElement
     *              адрес элемента
     */
    public void click(String pathToElement) {
        // элемент, который хотим нажать
        click(getLocatorByPath(pathToElement));
    }

    /**
     * Клик c ожиданием на элемент html-страницы
     *
     * @param locator
     *            адрес элемента
     */
    public void click(By locator) {

        try {
            // делаем попытки нажать на элемент
            long tm = System.currentTimeMillis();
            long end = tm + (TIMEOUT * 1000);
            do {
                WebElement element = getElement(locator);
                if (element != null && element.isEnabled()) {
                    // перемещаемся к элементу
                    scrollToElement(element);
                    if (element.isDisplayed()) {
                        element.click();
                        break;
                    }
                }
            } while ((System.currentTimeMillis()) <= end && sleep(100));
        } catch (Exception e) {

        }
    }

    /**
     * Скроллинг к нужному элементу
     *
     * @param pathToElement адрес элемента
     */
    public void scrollToElement(String pathToElement) {
        scrollToElement(getLocatorByPath(pathToElement));
    }

    /**
     * Скроллинг к нужному элементу
     *
     * @param locator адрес элемента
     */
    public void scrollToElement(By locator) {
        scrollToElement(getElementWait(locator, TIMEOUT));
    }

    /**
     * Скроллинг к нужному элементу
     *
     * @param element элемент
     */
    public WebElement scrollToElement(WebElement element) {

        RemoteWebElement elem = (RemoteWebElement) element;
        elem.getCoordinates().inViewPort();
        return elem;
    }

    /**
     * Обертка над sleep
     *
     * @param millis
     *          время сна в миллисекундах
     */
    public static boolean sleep(long millis) {
        if (millis < 0)
            throw new IllegalArgumentException();
        boolean result = true;
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            result = false;
        }
        return result;
    }

    /**
     * Получение элемента с ожиданием
     *
     * @param locator
     *          адрес элемента
     * @param time
     *          время ожидания в секундах
     * @return {@link WebElement}
     */
    public WebElement getElementWait(final By locator, long time) {
        return new WebDriverWait(getDriver(), time, SLEEPINMILS).until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver webDriver) {
                try {
                    WebElement element = getElement(locator);
                    return element != null ? element : null;
                } catch (Exception e) {
                }
                return null;
            }
        });
    }

    /**
     * Ожидание прогрузки страницы
     */
    public void waitForPageLoaded() {
        ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
                    }
                };
        try {
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(expectation);
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for Page Load Request to complete.");
        }
    }


    /**
     * Ввести текст в Веб-элемент
     *
     * @param pathToElement
     *          адрес элемента
     * @param text
     *          вводимый текст
     * @return {@WebElement}
     */
    public WebElement inputText(String pathToElement, String text) {
        return inputText(getLocatorByPath(pathToElement), text);
    }

    /**
     * Ввести текст в Веб-элемент
     *
     * @param locator
     *          адрес элемента
     * @param text
     *          вводимый текст
     * @return {@WebElement}
     */
    public WebElement inputText(By locator, String text) {
        return inputText(getElementWait(locator, TIMEOUT), text);
    }

    /**
     * Вводит текст в поле
     * Предварительно поле очищается от предыдущих значений
     *
     * @param element
     *          элемент
     * @param text
     *          текст для ввода
     * @return {@link WebElement}, в который вводили текст
     */
    public WebElement inputText(final WebElement element, String text) {
        if (element == null)
            throw new IllegalArgumentException();
        if (text == null)
            throw new IllegalArgumentException();
        scrollToElement(element);
        if (element.isDisplayed())
            element.click();
        try {
            element.clear();
        } catch (Exception e) {
            // выбрать весь текст
            element.sendKeys(Keys.CONTROL, "a", Keys.BACK_SPACE);
        }
        try {
            sendKeys(element, text);
        } catch (Exception e) {
        }
        return element;
    }

    /**
     * Послать нажатие клавиатуры на элемент
     *
     * @param element
     *          адрес элемента
     * @param keys
     *          символы
     */
    public void sendKeys(WebElement element, String keys) {
        if (element != null) {
            element.sendKeys(keys);
        }
    }

    /**
     * Очистить текст элемента
     *
     * @param pathToElement
     *          адрес элемента
     */
    public void clearText(String pathToElement) {
        clearText(getLocatorByPath(pathToElement));
        sleep(500);
    }

    /**
     * Очистить текст элемента
     *
     * @param locator
     *          адрес элемента
     */
    public void clearText(By locator) {
        WebElement element = getElementWait(locator, TIMEOUT);
        if (element != null)
            if (element.isDisplayed())
                element.click();
        try {
            element.clear();
        } catch (Exception e) {
            element.sendKeys(Keys.CONTROL, "a", Keys.BACK_SPACE);
        }
    }

    /**
     * Переход на главную страницу
     */
    public static void goToStartPage(WebDriverHelper wdh) {
        wdh.click("//span[@data-qa-file='TinkoffLogo']");
        wdh.waitForPageLoaded();
    }

}
