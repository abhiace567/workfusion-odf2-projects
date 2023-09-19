package com.workfusion.invoiceplane.automation.invoiceplane.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

import com.workfusion.odf2.core.webharvest.rpa.RpaDriver;
import com.workfusion.rpa.driver.Driver;
import com.workfusion.rpa.helpers.RPA;

public class ReportPage {

	private final Logger logger;
	private final Driver driver;
	
	@FindBy(xpath = "//input[@type='submit']")
	WebElement runReportBtn;
	
	public ReportPage(Driver driver, Logger logger) {
		this.logger = logger;
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	public void downloadReport(String filePath) {
		runReportBtn.click();
		logger.info("Run report Report botton clicked");
		
		driver.findElement(By.tagName("body"));
		
		driver.switchDriver(RpaDriver.DESKTOP.getName());
		driver.switchTo().window(".Chrome_WidgetWin_1[title='Invoice Aging - Google Chrome']");
		RPA.keyboard().sendKeys(Keys.chord(Keys.CONTROL, "s"));
		driver.switchTo().window("[title='Save As']");
		driver.findElement(By.cssSelector(".Edit")).sendKeys(filePath, Keys.ENTER);		
	}
}
