package com.workfusion.invoiceplane.automation.invoiceplane.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

import com.workfusion.rpa.driver.Driver;

public class MainPage {

	private final Driver driver;
	private final Logger logger;
	
	@FindBy(xpath = "//div[@id='ip-navbar-collapse']")
	WebElement navbar;
	
	public MainPage(Driver driver, Logger logger) {
		this.driver = driver;
		this.logger = logger;
		PageFactory.initElements(driver, this);
	}
	
	public ReportPage navigateToReportPage() {
		WebElement menuItem = navbar.findElement(By.partialLinkText("Reports"));
		menuItem.click();
		menuItem.findElement(By.xpath("//a[contains(text(), 'Invoice Aging')]")).click();
		return new ReportPage(driver, logger);
	}
}
