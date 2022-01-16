package ui.desktop.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import ui.abstractClasses.AbstractPage;
import ui.driver.SingletonDriver;

public class CartPage extends AbstractPage {
    public static final String PAGE_URL = BASE_URL + "/cart";

    public CartPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void openCartPage() {
        SingletonDriver.getDriver().get(PAGE_URL);
    }


}
