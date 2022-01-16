package ui.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import java.util.concurrent.TimeUnit;


import static ui.constants.Constants.IMPLICITLY_WAIT_TIMEOUT;
import static ui.driver.CapabilitiesHelper.getChromeOptions;

public class SingletonDriver {
    private static WebDriver driver;

    private SingletonDriver() {
    }

    public static synchronized WebDriver getDriver() {
        String driverName = System.getProperty("ui/driver");
        if (driver == null) {
            if ("fireFox".equals(driverName)) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            } else if ("ie".equals(driverName)) {
                WebDriverManager.iedriver().setup();
                driver = new InternetExplorerDriver();
            } else if ("edge".equals(driverName)) {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
            } else {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver(getChromeOptions());
            }
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(IMPLICITLY_WAIT_TIMEOUT, TimeUnit.SECONDS);
        return driver;
    }
}
