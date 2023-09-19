package com.workfusion.invoiceplane.module;

import javax.inject.Singleton;

import org.codejargon.feather.Provides;

import com.workfusion.bot.service.SecureEntryDTO;
import com.workfusion.invoiceplane.service.EmailService;
import com.workfusion.odf2.core.cdi.OdfModule;
import com.workfusion.odf2.service.s3.S3Bucket;
import com.workfusion.odf2.service.s3.S3Service;
import com.workfusion.odf2.service.vault.SecretsVaultService;

public class ServiceModule implements OdfModule {

	@Provides
	@Singleton
	public EmailService emailService(SecretsVaultService secretsVault) {
		SecureEntryDTO entry = secretsVault.getEntry("519968_mail");
		return new EmailService("smtp.office365.com", "587", entry.getKey(), entry.getValue());
	}
	
	@Provides
	@Singleton
	public S3Bucket getS3Bucket(S3Service s3Service) {
		return s3Service.getBucket("519968");
	}
}
