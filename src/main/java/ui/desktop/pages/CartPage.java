package ui.desktop.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import ui.abstractClasses.AbstractPage;
import ui.driver.SingletonDriver;

public class CartPage extends AbstractPage {
    public static final String PAGE_URL = BASE_URL + "/cart";
    public static final String expectedProductName = "Kruidvat Sensitive Handzeep Navulling";

    @FindBy(xpath = "//div[@class='product-summary__description-name']")
    private WebElement productName;

    public CartPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void openCartPage() {
        SingletonDriver.getDriver().get(PAGE_URL);
    }

    public void verifyProductName() {
        Assert.assertEquals(productName.getText(), expectedProductName);
    }

}
