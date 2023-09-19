package com.ebay.products.client;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

import com.ebay.products.pages.DashboardPage;
import com.workfusion.automation.rpa.driver.DriverType;
import com.workfusion.automation.rpa.driver.DriverWrapper;
import com.workfusion.automation.rpa.driver.RobotDriverWrapper;
import com.workfusion.rpa.helpers.RPA;

public class EbayEcommerceClient {
	
	private final String EBAY_ECOMMERCE_PORTAL = "https://www.ebay.com/";
	private final DriverWrapper driverWrapper;
	
	public EbayEcommerceClient(Logger logger) {
		this.driverWrapper = new RobotDriverWrapper(logger);
	}
	
	public DashboardPage getDashboardPage() {
		driverWrapper.switchDriver(DriverType.CHROME);
		driverWrapper.getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		RPA.openChrome(EBAY_ECOMMERCE_PORTAL);
		return new DashboardPage(driverWrapper);
	}
}
