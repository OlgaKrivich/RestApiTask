package ui.desktop.fragments;

import static org.openqa.selenium.support.PageFactory.initElements;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ui.abstractClasses.AbstractFragment;

public class CruidCookiesFragment extends AbstractFragment {

    @FindBy(xpath = "//button[@id='onetrust-accept-btn-handler']")
    private WebElement acceptCookiesButton;

    @FindBy(xpath = "//button[@class='button  button--big']/span[contains(text(),'Naar Kruidvat.nl')]")
    private WebElement franceButton;

    public CruidCookiesFragment(WebDriver driver) {
        initElements(driver, this);
    }

    public void clickAcceptCookiesButton() {
        acceptCookiesButton.click();
    }

    public void clickFranceButton() {
        franceButton.click();
    }

}
