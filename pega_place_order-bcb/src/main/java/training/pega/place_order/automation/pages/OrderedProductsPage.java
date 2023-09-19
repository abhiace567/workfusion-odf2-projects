package training.pega.place_order.automation.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

import com.workfusion.rpa.driver.Driver;

public class OrderedProductsPage {
	
	private final Logger logger;
	private final Driver driver;

	@FindBy(xpath = "//input[@name='edit_your_cart']")
    WebElement editCart;
	
	public OrderedProductsPage(Driver driver, Logger logger) {
		this.logger = logger;
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public CartPage getCart() {
        editCart.click();
        return new CartPage(driver, logger);
    }
}
