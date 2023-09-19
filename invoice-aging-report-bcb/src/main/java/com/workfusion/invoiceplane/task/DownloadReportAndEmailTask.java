package com.workfusion.invoiceplane.task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.slf4j.Logger;

import com.workfusion.bot.service.SecureEntryDTO;
import com.workfusion.invoiceplane.automation.ReporterRobot;
import com.workfusion.invoiceplane.module.ServiceModule;
import com.workfusion.invoiceplane.service.EmailService;
import com.workfusion.odf2.compiler.BotTask;
import com.workfusion.odf2.core.cdi.Requires;
import com.workfusion.odf2.core.task.generic.GenericTask;
import com.workfusion.odf2.core.task.output.SingleResult;
import com.workfusion.odf2.core.task.output.TaskRunnerOutput;
import com.workfusion.odf2.core.webharvest.rpa.RpaDriver;
import com.workfusion.odf2.core.webharvest.rpa.RpaFactory;
import com.workfusion.odf2.core.webharvest.rpa.RpaRunner;
import com.workfusion.odf2.service.ControlTowerServicesModule;
import com.workfusion.odf2.service.s3.S3Bucket;
import com.workfusion.odf2.service.vault.SecretsVaultService;
import com.workfusion.rpa.helpers.RPA;

@BotTask(requireRpa=true)
@Requires({ControlTowerServicesModule.class, ServiceModule.class})
public class DownloadReportAndEmailTask implements GenericTask {

	private final Logger logger;
	private final S3Bucket s3Bucket;
	private final RpaRunner rpaRunner;
	private final SecretsVaultService secretsVaultService;
	private final EmailService emailService;
	
	@Inject	
	public DownloadReportAndEmailTask(Logger logger, S3Bucket s3Bucket, RpaFactory rpaFactory,
			SecretsVaultService secretsVaultService, EmailService emailService) {
		this.logger = logger;
		this.s3Bucket = s3Bucket;
		this.rpaRunner = rpaFactory.builder(RpaDriver.UNIVERSAL).closeOnCompletion(true).startInPrivate(true).build();
		this.secretsVaultService = secretsVaultService;
		this.emailService = emailService;
	}

	@Override
	public TaskRunnerOutput run() {
		SingleResult result = new SingleResult();
		SecureEntryDTO invoicePlaneEntryDTO = secretsVaultService.getEntry("invoice_plane_alias");

		rpaRunner.execute(driver -> {
			ReporterRobot reporterRobot = new ReporterRobot(driver, logger);
			String reportPath = reporterRobot.downloadInvoiceAgingReport(invoicePlaneEntryDTO);
			
			try {
				File report = new File(reportPath);
				logger.info("Uploading file to S3.");
				result.withColumn("Report", s3Bucket.put(Files.readAllBytes(report.toPath()),"Invoice Aging.pdf").getSanitizedDirectUrl());
				logger.info("Sending Email");
				emailService.sendEmail("abhijitnayak456@gmail.com", "Test email with Attachnet", "Report", Arrays.asList(report));
				
			} catch (IOException e) {
				logger.error("Report file not found", e.getLocalizedMessage());
			} catch (AddressException e) {
				logger.error("Can not send email due to address is not correct.", e.getLocalizedMessage());
			} catch (MessagingException e) {
				logger.error("Can not send email.", e.getLocalizedMessage());
			} finally {
				logger.info("Deleting report file from agent.");
				RPA.deleteFileOnAgent(reportPath);			
			}
			
		});

		return result;
	}

}
