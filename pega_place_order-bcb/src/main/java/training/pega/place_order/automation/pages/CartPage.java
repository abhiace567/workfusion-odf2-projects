package training.pega.place_order.automation.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;

import com.workfusion.rpa.driver.Driver;

import training.pega.place_order.model.BillingDetails;
import training.pega.place_order.model.CardDetails;

public class CartPage {
	
	private final Logger logger;
	private final Driver driver;
	
	@FindBy(id = "next1_button")
    WebElement next1;

    @FindBy(id = "bfirst_name")
    WebElement firstName;

    @FindBy(id = "blast_name")
    WebElement lastName;

    @FindBy(id = "bstreet_address")
    WebElement streetAddress;

    @FindBy(id = "bzip_code")
    WebElement zipCode;

    @FindBy(id = "barea_code")
    WebElement areaCode;

    @FindBy(id = "bprimary_phone")
    WebElement phone;

    @FindBy(linkText = "Ship to Billing Address")
    WebElement shipToLink;

    @FindBy(id = "next2_button")
    WebElement next2;

    @FindBy(id = "credit_card")
    WebElement creditCard;

    @FindBy(id = "card_type")
    WebElement cardType;

    @FindBy(id = "security_code")
    WebElement securityCode;

    @FindBy(id = "card_number")
    WebElement cardNumber;

    @FindBy(id = "expiry_month")
    WebElement expiryMonth;

    @FindBy(id = "expiry_year")
    WebElement expiryYear;

    @FindBy(xpath = "//input[@type='submit']")
    WebElement submit;
    
    @FindBy(xpath = "//span[@id='total_value']")
    WebElement totalPrice;

	public CartPage(Driver driver, Logger logger) {
		this.logger = logger;
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public String getTotalPrice() {
		return totalPrice.getText();
	}
	
	public OrderGeneratedPage completeOrder(BillingDetails billingDetails, CardDetails cardDetails) {
        next1.click();

        firstName.sendKeys(billingDetails.getFirstName());
        lastName.sendKeys(billingDetails.getLastName());
        streetAddress.sendKeys(billingDetails.getStreetAddress());
        zipCode.sendKeys("" + billingDetails.getZipCode());
        areaCode.sendKeys("" + billingDetails.getAreaCode());
        phone.sendKeys("" + billingDetails.getPhone());
        shipToLink.click();
        next2.click();

        creditCard.click();
        Select cardSelect = new Select(cardType);
        cardSelect.selectByVisibleText(cardDetails.getCardType());
        securityCode.sendKeys(""+ cardDetails.getSecurityCode());
        cardNumber.sendKeys("" + cardDetails.getCardNumber());
        Select monthSelect = new Select(expiryMonth);
        monthSelect.selectByIndex(cardDetails.getExpiryMonth());
        Select yearSelect = new Select(expiryYear);
        yearSelect.selectByValue("" + cardDetails.getExpiryYear());
        submit.click();

        return new OrderGeneratedPage(driver);
    }

}
