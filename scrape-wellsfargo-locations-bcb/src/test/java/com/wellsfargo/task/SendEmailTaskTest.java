package com.wellsfargo.task;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.workfusion.odf.test.junit.WorkerJUnitConfig;
import com.workfusion.odf.test.launch.BotTaskUnit;
import com.workfusion.odf2.junit.BotTaskFactory;

@WorkerJUnitConfig
class SendEmailTaskTest {

	@Test
	@DisplayName("shoud send email")
	void shoudSendEmail(BotTaskFactory botTaskFactory) {
		BotTaskUnit botTask = botTaskFactory.fromClass(SendEmailTask.class).withSecureEntries(
				secureEntry -> secureEntry.withEntry("519968_mail", "abhijit.nayak_2k22@outlook.com", "Astro@123"));
		
		Assertions.assertThatCode(() -> botTask.buildAndRun()).doesNotThrowAnyException();
	}

}
