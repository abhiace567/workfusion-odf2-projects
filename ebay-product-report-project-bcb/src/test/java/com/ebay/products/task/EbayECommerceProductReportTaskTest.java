package com.ebay.products.task;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.workfusion.odf.test.junit.IacDeveloperJUnitConfig;
import com.workfusion.odf.test.launch.BotTaskUnit;
import com.workfusion.odf.test.launch.InputData;
import com.workfusion.odf2.junit.BotTaskFactory;

@IacDeveloperJUnitConfig
class EbayECommerceProductReportTaskTest {

	@Test
	void testBot(BotTaskFactory botTaskFactory) {
		BotTaskUnit taskUnit = botTaskFactory.fromClass(EbayECommerceProductReportTask.class)
				.withS3(s3Settings -> s3Settings
				.withEndpointUrl("http://localhost:15110").withAccessKey("admin").withSecretKey("admin123"))
				.withInputData(InputData.fromResource("ebay-search-product.csv"));
	
		Assertions.assertThatCode(() -> taskUnit.buildAndRun()).doesNotThrowAnyException();
	}

}
