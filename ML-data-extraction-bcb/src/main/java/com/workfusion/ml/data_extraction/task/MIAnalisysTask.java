package com.workfusion.ml.data_extraction.task;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;

import com.workfusion.odf2.compiler.BotTask;
import com.workfusion.odf2.core.task.generic.GenericTask;
import com.workfusion.odf2.core.task.output.TaskRunnerOutput;
import com.workfusion.odf2.core.webharvest.rpa.RpaDriver;
import com.workfusion.odf2.core.webharvest.rpa.RpaFactory;
import com.workfusion.odf2.core.webharvest.rpa.RpaRunner;
import com.workfusion.rpa.helpers.RPA;

@BotTask(requireRpa = true)
public class MIAnalisysTask implements GenericTask {

	private final RpaRunner rpaRunner;
	private final Logger logger;

	@Inject
	public MIAnalisysTask(RpaFactory rpaFactory, Logger logger) {
		rpaRunner = rpaFactory.builder(RpaDriver.UNIVERSAL).startInPrivate(false).blockImages(false).maximizeOnStartup(true).closeOnCompletion(false).build();
		this.logger=logger;
	}

	@Override
	public TaskRunnerOutput run() {
		rpaRunner.execute(driver -> {
//			driver.switchDriver(RpaDriver.DESKTOP.getName());
//			driver.switchTo().window(".Chrome_WidgetWin_1[title=\"Bing AI - Search - Personal - Microsoft​ Edge\"]");
			driver.switchDriver(RpaDriver.EDGE.getName());
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS).pageLoadTimeout(90, TimeUnit.SECONDS);
			RPA.sleep(5_000);
			driver.navigate().to("https://www.bing.com/search?q=Bing+AI&showconv=1&FORM=hpcodx");
			try (XSSFWorkbook workBook = new XSSFWorkbook(
					new FileInputStream("C:\\Users\\abhij\\Desktop\\MI Analysis Input.xlsx"))) {
				XSSFSheet sheet = workBook.getSheetAt(0);

				for (int rownum = 1; rownum <= sheet.getLastRowNum(); rownum++) {
					Row row = sheet.getRow(rownum);
					StringBuilder sb = new StringBuilder("please summarise the following incident records to a table of \"Issue, Effect, Fix, Incident number, category\". keep the fix field blank if proper fix is not provided.\n\n");
					
					for (int cellnum = 0; cellnum < row.getLastCellNum(); cellnum++) {
						sb.append(row.getCell(cellnum).getStringCellValue()).append("\n");
					}
					logger.info("getting search element ****************");
					WebElement chatInputElement = (WebElement) driver.executeScript("document.querySelector(\"#b_sydConvCont > cib-serp\").shadowRoot.querySelector(\"#cib-action-bar-main\").shadowRoot.querySelector(\"div\")");
						
					WebElement searchbox = chatInputElement.findElement(By.id("searchbox"));
					searchbox.setText(sb.length() > 2000 ? sb.substring(0, 2000):sb.toString());
					searchbox.sendKeys(Keys.ENTER);
					break;
					
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		return null;
	}

}
