package com.wellsfargo.module;

import javax.inject.Singleton;

import org.codejargon.feather.Provides;

import com.wellsfargo.service.EmailService;
import com.workfusion.bot.service.SecureEntryDTO;
import com.workfusion.odf2.core.cdi.OdfModule;
import com.workfusion.odf2.service.vault.SecretsVaultService;

public class ServiceModule implements OdfModule {

	@Provides
	@Singleton
	public EmailService emailService(SecretsVaultService secretsVault) {
		SecureEntryDTO entry = secretsVault.getEntry("519968_mail");
		return new EmailService("smtp.office365.com", "587", entry.getKey(), entry.getValue());
	}
}
