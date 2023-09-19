package training.pega.place_order.automation.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;

import com.workfusion.rpa.driver.Driver;


public class ProductPage {
	
	private final Logger logger;
	private final Driver driver;

	@FindBy(xpath = "//input[@type='submit']")
    WebElement orderBtn;
	
	@FindBy(xpath = "//select[@name='product_quantity']")
	WebElement productQuantity;
	
	public ProductPage(Driver driver, Logger logger) {
		this.logger = logger;
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public OrderedProductsPage makeOrder(String quantity) {
		Select productQuantityList = new Select(this.productQuantity);
		logger.info("abel to select quantity---------------" + quantity);
        productQuantityList.selectByVisibleText(quantity);
        
        logger.info("selected quantyry : " + quantity );

        orderBtn.click();
        
        logger.info("orderbtn clicked");

        return new OrderedProductsPage(driver, logger);
    }

}
