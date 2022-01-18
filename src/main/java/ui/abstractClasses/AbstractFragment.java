package ui.abstractClasses;

import static ui.driver.SingletonDriver.getDriver;

import org.openqa.selenium.support.PageFactory;
import ui.driver.WebDriverWaiter;

public class AbstractFragment extends WebDriverWaiter {

    public AbstractFragment() {
        PageFactory.initElements(getDriver(), this);
    }

}
