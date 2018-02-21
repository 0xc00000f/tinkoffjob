package func;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

/**
 * Created by admin on 21.02.2018.
 */
public class WebDriverHelper {

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
     * Запускаем сервис для Chrome драйвера
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
     * Останавливаем сервис для Chrome
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
     * Закрываем драйвер
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

}
