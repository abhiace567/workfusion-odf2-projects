package com.workfusion.invoiceplane.task;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.workfusion.odf.test.junit.IacDeveloperJUnitConfig;
import com.workfusion.odf2.junit.BotTaskFactory;

@IacDeveloperJUnitConfig
class DownloadReportAndEmailTaskTest {

	@Test
	void test(BotTaskFactory botTaskFactory) {
		botTaskFactory.fromClass(DownloadReportAndEmailTask.class)
				.withSecureEntries(secureEntries -> secureEntries
						.withEntry("invoice_plane_alias", "wf-robot@mail.com", "BotsRock4ever!")
						.withEntry("519968_mail", "abhijit.nayak_2k22@outlook.com", "Astro@123"))
				.buildAndRun();
	}

}
