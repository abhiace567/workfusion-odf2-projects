package com.wellsfargo.automation.wellsfargo.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

import com.workfusion.rpa.driver.Driver;

public class LocatorPage {

	private final Driver driver;
	private final Logger logger;
	
	@FindBy(id = "mainSearchField")
	private WebElement searchBox;
	
	@FindBy(id = "mainSearchButton")
	private WebElement searchBtn;
	
	public LocatorPage(Driver driver, Logger logger) {
		this.driver = driver;
		this.logger = logger;
		PageFactory.initElements(driver, this);
	}
	
	public SearchResultsPage SearchAtms(String location) {
		searchBox.sendKeys(location);
		logger.info("Entered location in search box as - {}", location);
		
		searchBtn.click();
		logger.info("Search button Clicked");
		
		return new SearchResultsPage(driver, logger);
	}

}
