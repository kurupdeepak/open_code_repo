package com.echoworks.messaging;

import java.util.ArrayList;
import java.util.List;

import com.echoworks.messaging.core.DefaultMessageTransformer;
import com.echoworks.messaging.core.MailMessageInitializer;
import com.echoworks.messaging.core.MessageInitializer;
import com.echoworks.messaging.core.MessageTransformer;

public class Message {

	private String subject;

	private List<String> toEmail;

	private String fromEmail;

	private String body;

	private String fileName;

	MessageInitializer initializer;

	MessageTransformer rulesProcessor;
	
	String formattedText;

	Message(String fileName) {
		this.setFileName(fileName);
		this.setToEmail(new ArrayList<String>());
		initializer = new MailMessageInitializer(this);
		rulesProcessor = new DefaultMessageTransformer(this);
		initializer.init();
		rulesProcessor.process();
		formatMessage();
	}
	
	Message(String fileName,MessageTransformer ruleProcessor) {
		this.setFileName(fileName);
		this.setToEmail(new ArrayList<String>());
		initializer = new MailMessageInitializer(this);
		rulesProcessor = ruleProcessor;
		initializer.init();
		rulesProcessor.process(this);
		formatMessage();
	}
	
	private void formatMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append(InitializerConstants.TO.value()).append(emailTxt()).append(System.lineSeparator());
		sb.append(InitializerConstants.FROM.value()).append(getFromEmail()).append(System.lineSeparator());
		sb.append(InitializerConstants.SUBJECT.value()).append(getSubject()).append(System.lineSeparator());
		sb.append(InitializerConstants.BODY.value()).append(getBody());
		this.formattedText = sb.toString();
	}
	
	private String emailTxt() {
		StringBuilder sb = new StringBuilder();
		for(String email : getToEmail()) {
			sb.append(email).append(",");
		}
		return sb.substring(0,sb.length() - 1);
	}

	@Override
	public String toString() {
		return "Message file=" + getFileName() + System.lineSeparator() + formattedText;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String file) {
		this.fileName = file;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public List<String> getToEmail() {
		return toEmail;
	}

	public void setToEmail(List<String> toEmail) {
		this.toEmail = toEmail;
	}

	public MessageTransformer getRulesProcessor() {
		return rulesProcessor;
	}

	public void setRulesProcessor(DefaultMessageTransformer rulesProcessor) {
		this.rulesProcessor = rulesProcessor;
	}
	
}