package com.ebay.products.task;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.ebay.products.common.GenericTaskMultipleResults;
import com.ebay.products.pages.workflow.ProductSearchPage;
import com.workfusion.odf2.compiler.BotTask;
import com.workfusion.odf2.core.cdi.Requires;
import com.workfusion.odf2.core.task.TaskInput;
import com.workfusion.odf2.core.webharvest.rpa.RpaDriver;
import com.workfusion.odf2.core.webharvest.rpa.RpaFactory;
import com.workfusion.odf2.core.webharvest.rpa.RpaRunner;
import com.workfusion.odf2.service.s3.S3Module;
import com.workfusion.odf2.service.s3.S3Service;

import groovy.util.logging.Slf4j;

@Slf4j
@BotTask(requireRpa = true)
@Requires(S3Module.class)
public class EbayECommerceProductReportTask implements GenericTaskMultipleResults {

	private final String INPUT_COLUMN_NAME = "product";
	private RpaRunner rpaRunner;
	private TaskInput taskInput;
	private Logger logger;
	private S3Service s3Service;
	
	@Inject
	public EbayECommerceProductReportTask(RpaFactory rpaFactory, TaskInput taskInput, Logger logger, S3Service s3Service) {
		this.rpaRunner = rpaFactory.builder(RpaDriver.UNIVERSAL).closeOnCompletion(true).build();
		this.taskInput = taskInput;
		this.logger = logger;
		this.s3Service = s3Service;
	}

	@Override
	public List<Map<String, String>> run() {
		AtomicReference<List<Map<String, String>>> resultSet = new AtomicReference<>();
		rpaRunner.execute(driver -> {
			String productName = taskInput.getRequiredVariable(INPUT_COLUMN_NAME);
			List<Map<String, String>> results = new ProductSearchPage().searchProduct(productName, s3Service, logger);
			resultSet.set(results);
		});
		return resultSet.get();
	}

}
