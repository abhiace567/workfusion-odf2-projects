package com.ebay.products.pages.workflow;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.ebay.products.client.EbayEcommerceClient;
import com.ebay.products.dto.ProductDTO;
import com.ebay.products.pages.DashboardPage;
import com.ebay.products.pages.ResultsPage;
import com.workfusion.odf2.service.s3.S3Service;

public class ProductSearchPage {

	public List<Map<String, String>> searchProduct(String productName, S3Service s3Service, Logger logger) {
		EbayEcommerceClient ecommerceClient = new EbayEcommerceClient(logger);
		DashboardPage dashboardPage = ecommerceClient.getDashboardPage();
		ResultsPage resultsPage = dashboardPage.makeSearch(productName);
		List<ProductDTO> products = resultsPage.getProductDetails(logger);
		return dashboardPage.saveProducts(products, s3Service);
	}

}
