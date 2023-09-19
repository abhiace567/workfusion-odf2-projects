package com.wellsfargo.automation.wellsfargo.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;

import com.workfusion.rpa.driver.Driver;

public class SearchResultsPage {

	private final Logger logger;

	@FindBy(xpath = "//*[@id='searchResultsList']/li/div/div[@itemprop='location']")
	private List<WebElement> locations;

	public SearchResultsPage(Driver driver, Logger logger) {
		this.logger = logger;
		PageFactory.initElements(driver, this);
	}

	public List<Map<String, String>> extractLocationDetails() {
		logger.info("Total locations forund - {}", locations.size());
		
		return locations.stream().limit(10).map(location -> {
			return new HashMap<String, String>() {
				private static final long serialVersionUID = 1L;
				{
					put("Location Name",
							location.findElement(By.xpath("address/div[@itemprop='addressLocality']")).getText());
					put("Address",
							location.findElement(By.xpath("address/div[@itemprop='addressRegion']")).getText());
					put("City", location.findElement(By.xpath("address/div/span[@itemprop='addressLocality']"))
							.getText());
					put("State", location.findElement(By.xpath("address/div/*[@itemprop='addressRegion']"))
							.getAttribute("title"));
					put("Phone", location.findElement(By.xpath("div[@itemprop='telephone']")).getText());
				}
			};
		}).collect(Collectors.toList());
	}

}
