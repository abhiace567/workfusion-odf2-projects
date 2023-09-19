package com.wellsfargo.automation.wellsfargo;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.wellsfargo.automation.wellsfargo.pages.LocatorPage;
import com.workfusion.rpa.driver.Driver;

public class WellsfargoClient {
	
	private final String WELLSFARGO_LOCATOR_URL = "https://www.wellsfargo.com/locator/";
	
	private final Driver driver;
	private final Logger logger ;
	
	public WellsfargoClient(Driver driver, Logger logger) {
		this.driver = driver;
		this.logger = logger;
		initDriver();
	}

	private void initDriver() {
		logger.info("Configuring WebDriver");
		driver.switchDriver("chrome");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS).pageLoadTimeout(90, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
	}

	public LocatorPage getWellsfargoLocatorPage() {
		logger.info("Opening Wellsfargo Locator Page");
		driver.navigate().to(WELLSFARGO_LOCATOR_URL);
		return new LocatorPage(driver, logger);
	}
}
