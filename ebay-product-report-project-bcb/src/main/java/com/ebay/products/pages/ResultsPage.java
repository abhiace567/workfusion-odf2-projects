package com.ebay.products.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;

import com.ebay.products.dto.ProductDTO;
import com.workfusion.automation.rpa.driver.DriverWrapper;

public class ResultsPage extends BasePage {

	public ResultsPage(DriverWrapper driver) {
		super(driver);
	}

	public List<ProductDTO> getProductDetails(Logger logger) {

		List<String> productLinks = new ArrayList<>();
		List<WebElement> pagenationEles = driver
				.findElements(By.xpath("//ol[contains(@class, 'pagination__items')]/li"));

		int currentPage = 1;
		if (pagenationEles.size() > 3) {
			while (currentPage <= 3) {
				String expression = String.format("//ol[contains(@class, \"pagination__items\")]/li[%d]", currentPage);
				driver.findElement(By.xpath(expression)).click();

				for (int i = 2; i < 12; i++) {
					String productLinkExp = String.format("//*[@id=\"srp-river-results\"]/ul/li[%d]/div/div[2]/a", i);
					WebElement productLinkEle = driver.findElement(By.xpath(productLinkExp));
					productLinks.add(productLinkEle.getAttribute("href"));
				}

				currentPage++;
			}
		}

		return readProductDetails(productLinks, logger);
	}

	private List<ProductDTO> readProductDetails(List<String> productLinks, Logger logger) {
		String productName = "";
		String itemSpecifics = "";
		String decision = "";
		String productLink = "";

		List<ProductDTO> productDtos = new ArrayList<>();

		// switching to tabs
//		Set<String> tabs = driver.getWindowHandles();
//		int totalTabs = tabs.size();
//		int newTabIndex = totalTabs - 1;
//		driver.switchToWindow(tabs.toArray()[newTabIndex].toString());

		for (int i = 0; i < productLinks.size(); i++) {
			productLink = productLinks.get(i);
			
			driver.getDriver().navigate().to(productLink);
			
			try {
				productName = driver.findElement(By.xpath("//*[@id=\"LeftSummaryPanel\"]/div[1]/div[1]/div/h1"))
						.getText();
				if (productName.contains(",")) {
					productName = productName.substring(0, productName.indexOf(","));
				} else if (productName.contains("BY".toLowerCase())) {
					productName = productName.substring(0, productName.indexOf("by"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				WebElement itemSpecificEle = driver.findElement(By.xpath("//*[@id='ABOUT_THIS_ITEM0-0-1-1-title']"));
				if (itemSpecificEle != null && "Item specifics".equalsIgnoreCase(itemSpecificEle.getText())) {
					List<WebElement> itemSpecificRows = driver
							.findElements(By.xpath("//*[@id=\"viTabs_0_is\"]/div/div[2]/div/div"));
					Map<String, String> map = new HashMap<>();
					for (WebElement itemspecificRow : itemSpecificRows) {
						String[] contentArr = itemspecificRow.getText().split("\n");
						if (contentArr.length == 5) {
							map.put(contentArr[0], contentArr[1]);
							map.put(contentArr[3], contentArr[4]);
						} else if (contentArr.length == 4) {
							map.put(contentArr[0], contentArr[1]);
							map.put(contentArr[2], contentArr[3]);
						} else {
							map.put(contentArr[0], contentArr[1]);
						}
					}
					itemSpecifics = map.entrySet().stream().map(Object::toString).collect(Collectors.joining("|")).replaceAll("=", " ");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				String positiveFeedback = driver.findElement(By.xpath("//*[contains(@class, 'x-about-this-seller')]/div/div[2]/ul/li[2]")).getText();
				if (StringUtils.isNotBlank(positiveFeedback)) {
					decision = getDecision(positiveFeedback, logger, driver) ? "approve" : "disapprove";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ProductDTO productDTO = new ProductDTO();
			productDTO.setProductLink(productLink);
			productDTO.setFullName(productName);
			productDTO.setItemDetails(itemSpecifics);
			productDTO.setDecision(decision);
			
			productDtos.add(productDTO);
		}
		
		return productDtos;
	}

	private boolean getDecision(String positiveFeedback, Logger logger, DriverWrapper driver) {
		boolean hasToApprove = false;
		try {
			String shipping = driver.findElement(By.xpath("//*[@id=\"SRPSection\"]/div[1]/div/div/div/div/div[1]/div/div/div[2]/div/div")).getText();
			if(StringUtils.isNoneBlank(shipping) && shipping.toLowerCase().contains("free")
					&& Double.parseDouble(positiveFeedback.substring(0, positiveFeedback.indexOf("%"))) >= 99.0 ) {
				hasToApprove = true;
			}
		} catch (Exception e) {
			logger.warn("Positive Feedback or Free shipping element not found: {}", e.getLocalizedMessage());
		}
		
		return hasToApprove;
	}

}
