package training.pega.place_order.automation.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;

import com.workfusion.rpa.driver.Driver;

public class HomePage {
	
	private final Logger logger;
	private final Driver driver;
	
	@FindBy(id = "productType")
    WebElement productType;

    @FindBy(css = "select#productsList")
    WebElement product;

    @FindBy(id = "viewButton")
    WebElement viewBtn;

	public HomePage(Driver driver, Logger logger) {
		this.logger = logger;
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public ProductPage viewProduct(String productType, String product) {
        Select productTypeList = new Select(this.productType);
        productTypeList.selectByVisibleText(productType);

        Select productList = new Select(this.product);
        productList.selectByVisibleText(product);

        viewBtn.click();
        
        return new ProductPage(driver, logger);
	}
}
