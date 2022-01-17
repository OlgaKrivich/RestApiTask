package ui.hooks;

import org.openqa.selenium.Cookie;
import org.testng.annotations.AfterTest;
import ui.driver.SingletonDriver;

import static api.utils.Storage.getInstance;

public class DriverHooks {

    @AfterTest
    public void tearDown() {
        if (SingletonDriver.getDriver() != null) {
            SingletonDriver.getDriver().quit();
        }
    }

    public static void setAuthHeaders() {
        SingletonDriver.getDriver().manage().deleteAllCookies();
        SingletonDriver.getDriver().manage().addCookie(new Cookie("kvn-cart",
                getInstance().whatIsThe("guid").toString()));
    }

}
