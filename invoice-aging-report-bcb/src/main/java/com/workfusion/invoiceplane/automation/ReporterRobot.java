package com.workfusion.invoiceplane.automation;

import java.nio.file.Paths;

import org.slf4j.Logger;

import com.workfusion.bot.service.SecureEntryDTO;
import com.workfusion.invoiceplane.automation.invoiceplane.InvoicePlaneClient;
import com.workfusion.invoiceplane.automation.invoiceplane.pages.LoginPage;
import com.workfusion.invoiceplane.automation.invoiceplane.pages.MainPage;
import com.workfusion.invoiceplane.automation.invoiceplane.pages.ReportPage;
import com.workfusion.rpa.driver.Driver;

public class ReporterRobot {

	private final Driver driver;
	private final Logger logger;

	public ReporterRobot(Driver driver, Logger logger) {
		this.driver = driver;
		this.logger = logger;
	}

	public String downloadInvoiceAgingReport(SecureEntryDTO secureEntryDTO) {
		InvoicePlaneClient invoicePlaneClient = new InvoicePlaneClient(driver, logger);
		LoginPage loginPage = invoicePlaneClient.getLoginPage();
		MainPage mainPage = loginPage.performLogin(secureEntryDTO);
		ReportPage reportPage = mainPage.navigateToReportPage();
		String filePath = Paths.get(System.getProperty("user.dir"), "519968_Invoice Aging.pdf").toString();
		reportPage.downloadReport(filePath);
	
		return filePath;
	}
}
