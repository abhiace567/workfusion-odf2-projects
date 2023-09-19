package training.pega.place_order.automation.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

import com.workfusion.bot.service.SecureEntryDTO;
import com.workfusion.rpa.driver.Driver;

public class SigninPage {
	
	private final Logger logger;
	private final Driver driver;

	@FindBy(xpath = "//input[@id='user_name']")
    private WebElement userName;

    @FindBy(xpath = "//input[@id='user_pass']")
    private WebElement userPassword;

    @FindBy(xpath = "//input[@type='submit']")
    private WebElement signInBtn;
    
	public SigninPage(Driver driver, Logger logger) {
		this.logger = logger;
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public HomePage signIn(SecureEntryDTO creds) {
		this.userName.sendKeys(creds.getKey());
        this.userPassword.sendKeys(creds.getValue());
        signInBtn.click();

        if(!driver.getTitle().equals("Home | Pega Studio Training Web Application")) {
            logger.debug("Sign in error");
            return null;
        }

        return new HomePage(driver, logger);
	}

}
