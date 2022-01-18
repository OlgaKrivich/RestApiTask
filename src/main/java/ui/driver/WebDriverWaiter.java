package ui.driver;

import static ui.driver.SingletonDriver.getDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebDriverWaiter {

    static WebDriverWait wait = new WebDriverWait(getDriver(), 5);

    public WebDriverWaiter() {
    }

    public static void waitUntilElementToBeClickable(WebElement webElement) {
        wait.until(ExpectedConditions.elementToBeClickable(webElement));
    }
}
