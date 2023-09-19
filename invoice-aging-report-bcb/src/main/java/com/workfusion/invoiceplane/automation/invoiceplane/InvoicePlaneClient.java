package com.workfusion.invoiceplane.automation.invoiceplane;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.workfusion.invoiceplane.automation.invoiceplane.pages.LoginPage;
import com.workfusion.odf2.core.webharvest.rpa.RpaDriver;
import com.workfusion.rpa.driver.Driver;

public class InvoicePlaneClient {
	
	private final String INVOICE_PLANE_URL = "https://train-invoiceplane.workfusion.com/"; 
	private final Driver driver;
	private final Logger logger;
	
	public InvoicePlaneClient(Driver driver, Logger logger) {
		this.driver = driver;
		this.logger = logger;
		initDriver();
	}
	
	// Configure driver
	private void initDriver() {
		logger.info("Configuring WebDriver");
		driver.switchDriver(RpaDriver.CHROME.getName());
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS).pageLoadTimeout(90, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
	}
	
	public LoginPage getLoginPage() {
		logger.info("opening login page");
		driver.navigate().to(INVOICE_PLANE_URL);
		return new LoginPage(driver, logger);
	}
}
