package ui.driver;

public class WebDriverUtils {

    private WebDriverUtils() {
    }

    public static void refreshPage() {
        SingletonDriver.getDriver().navigate().refresh();
    }
}

