package training.pega.place_order.automation.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

import com.workfusion.rpa.driver.Driver;

public class MenuNavigationBar {
	
	private final Logger logger;
	private final Driver driver;
	
	@FindBy(xpath = "//*[@id='acme_home']/a")
	WebElement homeLink;

	@FindBy(xpath = "//*[@id='cart_menu']/a")
	WebElement cartLink;
	
	@FindBy(xpath = "//*[@id='profile_menu']")
	WebElement profileMenu;
	
	@FindBy(linkText = "Sign Out")
	WebElement linkSignOut;
	
	public MenuNavigationBar(Driver driver, Logger logger) {
		this.logger = logger;
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public HomePage getHomePage() {
		homeLink.click();
		return new HomePage(driver, logger);
	}
	
	public CartPage getCartPage() {
		cartLink.click();
		return new CartPage(driver, logger);
	}
	
	public void logout() {
		Actions actions = new Actions(driver);
		
		actions.moveToElement(profileMenu).perform();
		linkSignOut.click();
		
		
	}
}
