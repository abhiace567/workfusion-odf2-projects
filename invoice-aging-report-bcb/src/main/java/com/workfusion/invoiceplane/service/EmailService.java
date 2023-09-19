package com.workfusion.invoiceplane.service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailService {

	private final Session session;
	private final String userEmail;

	public EmailService(String host, String port, String username, String password) {
		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// Get the Session object.// and pass
		session = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		userEmail = username;
	}

	public void sendEmail(String recipientTo, String subject, String body)
			throws AddressException, MessagingException, IOException {
		sendEmail(recipientTo, "", "", subject, body, Collections.emptyList());
	}

	public void sendEmail(String recipientTo, String subject, String body, List<File> attachments)
			throws AddressException, MessagingException, IOException {
		sendEmail(recipientTo, "", "", subject, body, attachments);
	}

	public void sendEmail(String recipientTo, String recipientCC, String recipientBCC, String subject, String body,
			List<File> attachments) throws AddressException, MessagingException, IOException {
		// Create a default MimeMessage object.
		MimeMessage message = new MimeMessage(session);

		message.setFrom(new InternetAddress(userEmail));

		message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientTo));
		message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(recipientCC));
		message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(recipientBCC));

		// Set Subject
		message.setSubject(subject);

		// creating multipart message
		Multipart msgParts = new MimeMultipart();

		// setting message body
		MimeBodyPart textPart = new MimeBodyPart();
		textPart.setText(body, "UTF-8", "HTML");
		msgParts.addBodyPart(textPart);

		if (!attachments.isEmpty()) {
			MimeBodyPart attachmentPart = new MimeBodyPart();
			for (File attachment : attachments) {
				attachmentPart.attachFile(attachment);
			}
			msgParts.addBodyPart(attachmentPart);
		}

		message.setContent(msgParts);

		// Send message
		Transport.send(message);
	}
}
