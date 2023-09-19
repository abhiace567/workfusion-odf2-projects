package com.ebay.products.pages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.By;

import com.ebay.products.dto.ProductDTO;
import com.workfusion.automation.rpa.driver.DriverWrapper;
import com.workfusion.automation.rpa.elements.web.common.Button;
import com.workfusion.automation.rpa.elements.web.common.TextBox;
import com.workfusion.component.files.excel.SimpleExcelGenerator;
import com.workfusion.odf2.service.s3.S3Bucket;
import com.workfusion.odf2.service.s3.S3Service;
import com.workfusion.rpa.helpers.RPA;

public class DashboardPage extends BasePage {

	TextBox searchTextBox = new TextBox(driver, By.id("gh-ac"), "Product Search TextBox");
	Button searchBtn = new Button(driver, By.id("gh-btn"), "Search Botton ID");
	
	public DashboardPage(DriverWrapper driver) {
		super(driver);
	}
	
	public ResultsPage makeSearch(String searchText) {
		searchTextBox.setText(searchText);
		RPA.pressTab();
		searchBtn.click();
		return new ResultsPage(driver);
	}

	public List<Map<String, String>> saveProducts(List<ProductDTO> products, S3Service s3Service) {
		List<Map<String, String>> resultset = new ArrayList<>();
		String productReportUrl = "";
		Map<String, String> resultMap = new HashMap<>();
 		List<Map<String, String>> contents = ProductDTO.convertToMap(products);
		try {
			File excelFile = createExcelFile(contents);
			byte[] content = FileUtils.readFileToByteArray(excelFile);
			final S3Bucket s3Bucket = s3Service.getBucket("519968");
			String filePathToUpload = FilenameUtils.getFullPath(excelFile.getPath());
			productReportUrl = s3Bucket.put(content, filePathToUpload).getDirectUrl();
			resultMap.put("Product_Report_Url", productReportUrl);
			resultset.add(resultMap);
		} catch (Exception e) {
			driver.getLogger().error(e.getLocalizedMessage());
		}
		return resultset;
	}

	private File createExcelFile(List<Map<String, String>> rows) throws IOException {
		Path tempFile = Files.createTempFile("ebay-products-report", ".xlsx");
		File file = tempFile.toFile();
		System.out.println("File Path = " + file.getPath());
		file.deleteOnExit();
		
		FileOutputStream out = new FileOutputStream(file);
		
		List<String> headers = Arrays.asList("fullName", "itemDetails", "decision", "productLink");
		
		SimpleExcelGenerator.generate(out, headers, rows);
		
		return file;
	}
}
