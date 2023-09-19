package com.workfusion.invoiceplane.automation.invoiceplane.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

import com.workfusion.bot.service.SecureEntryDTO;
import com.workfusion.rpa.driver.Driver;

public class LoginPage {
	
	private final Driver driver;
	private final Logger logger;
	
	@FindBy(id = "email")
	WebElement emailText;
	
	@FindBy(id = "password")
	WebElement passwordText;
	
	@FindBy(xpath = "//input[@type='submit']")
	WebElement loginBtn;
	
	public LoginPage(Driver driver, Logger logger) {
		this.driver = driver;
		this.logger = logger;
		PageFactory.initElements(driver, this);
	}
	
	public MainPage performLogin(SecureEntryDTO secureEntryDTO) {
		emailText.sendKeys(secureEntryDTO.getKey());
		passwordText.sendKeys(secureEntryDTO.getValue());
		loginBtn.click();
		return new MainPage(driver, logger);
	}

}
