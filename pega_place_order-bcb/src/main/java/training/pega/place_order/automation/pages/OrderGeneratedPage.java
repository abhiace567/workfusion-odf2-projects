package training.pega.place_order.automation.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.workfusion.rpa.driver.Driver;

public class OrderGeneratedPage {
	

	@FindBy(xpath = "//div[@class='content']/div/p/b")
	WebElement order;

	public OrderGeneratedPage(Driver driver) {
		PageFactory.initElements(driver, this);
	}

	public String getOrderId() {
		return order.getText().split(" #")[1];
	}

}
