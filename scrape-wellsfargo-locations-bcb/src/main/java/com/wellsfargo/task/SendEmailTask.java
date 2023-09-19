package com.wellsfargo.task;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.inject.Inject;
import javax.mail.MessagingException;

import org.slf4j.Logger;

import com.wellsfargo.module.ServiceModule;
import com.wellsfargo.service.EmailService;
import com.workfusion.odf2.compiler.BotTask;
import com.workfusion.odf2.core.cdi.Requires;
import com.workfusion.odf2.core.task.TaskInput;
import com.workfusion.odf2.core.task.generic.GenericTask;
import com.workfusion.odf2.core.task.output.SingleResult;
import com.workfusion.odf2.core.task.output.TaskRunnerOutput;
import com.workfusion.odf2.service.ControlTowerServicesModule;

@BotTask
@Requires({ControlTowerServicesModule.class, ServiceModule.class})
public class SendEmailTask implements GenericTask {
	
	private final EmailService emailService;
	private final Logger log;
	private final TaskInput input;
	
	@Inject
	public SendEmailTask(EmailService emailService, Logger log, TaskInput input) {
		this.emailService = emailService;
		this.log = log;
		this.input = input;
	}

	@Override
	public TaskRunnerOutput run() {
		try {
			log.info("Sending Email");
//			emailService.sendEmail("tuk.tuk.rpa@gmail.com", "Test email", "Hello World");
			emailService.forwordEmail("abhijitnayak456@gmail.com", "Test email with attachment");
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input.asResult();
	}

}
