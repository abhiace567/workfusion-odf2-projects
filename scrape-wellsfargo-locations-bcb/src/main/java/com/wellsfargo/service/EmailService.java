package com.wellsfargo.service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MimeType;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Provider;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import javax.ws.rs.ext.Providers;

import org.springframework.http.codec.ClientCodecConfigurer.MultipartCodecs;

public class EmailService {

	private final Session session;
	private final String userEmail;
	private String userPass = "Astro@123";

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

	public void forwardEmail(String recipientTo, String subject) throws NoSuchProviderException, MessagingException, IOException {
		Store store = session.getStore("imaps");
		store.connect("outlook.office365.com", 993, userEmail, userPass);
		Folder inbox = store.getFolder("INBOX");
		inbox.open(Folder.READ_WRITE);
		
		SearchTerm searchTerm = new AndTerm(new SubjectTerm(subject),
				new FlagTerm(new Flags(Flags.Flag.SEEN), false));

		Message[] messages = inbox.search(searchTerm);
		
		Message originalMessage = messages[0];
		

		// create a new message for forwarding
	    Message forward = new MimeMessage(session);
	    forward.setSubject("FW: " + subject);
	    
	    forward.setFrom(new InternetAddress(userEmail));
	    
	    // set the recipient of the forwarded message
	    forward.setRecipient(RecipientType.TO, new InternetAddress(recipientTo));
	    
	    Multipart forwardParts = new MimeMultipart();
	    
//	    BodyPart body = new MimeBodyPart();
//	    body.setText("Forwarding email\n\n");
//	    forwardParts.addBodyPart(body);
	    
	    Object content = originalMessage.getContent();
	    if(content instanceof Multipart) {
	    	Multipart multipartContent = (Multipart) content;
  
	    	for (int i = 0; i<multipartContent.getCount(); i++) {
//	    		System.out.println(multipartContent.getBodyPart(i).getContentType());
	    		forwardParts.addBodyPart(multipartContent.getBodyPart(i));
	    	  }
	      }

	    forward.setContent(forwardParts);
	    
	    // set the flags of the original message to mark it as forwarded
	    originalMessage.setFlag(Flag.SEEN, true);
	    

	    Transport.send(forward);
	    
	    // close the inbox folder
	    inbox.close(false);
	    store.close();
	}
}
