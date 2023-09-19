package com.wellsfargo.automation;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.wellsfargo.automation.wellsfargo.WellsfargoClient;
import com.wellsfargo.automation.wellsfargo.pages.LocatorPage;
import com.wellsfargo.automation.wellsfargo.pages.SearchResultsPage;
import com.workfusion.rpa.driver.Driver;

public class LocationScrapperRobot {

	private final Driver driver;
	private final Logger logger;

	public LocationScrapperRobot(Driver driver, Logger logger) {
		this.driver = driver;
		this.logger = logger;
	}
	
	public List<Map<String, String>> getAtmLocations() {
		WellsfargoClient client = new WellsfargoClient(driver, logger);
		LocatorPage wellsfargoLocatorPage = client.getWellsfargoLocatorPage();
		SearchResultsPage searchResultsPage = wellsfargoLocatorPage.SearchAtms("Austin, Travis County, TX");
		
		return searchResultsPage.extractLocationDetails();
	}
}
