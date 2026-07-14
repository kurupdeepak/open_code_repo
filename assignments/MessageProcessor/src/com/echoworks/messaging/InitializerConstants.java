package com.echoworks.messaging;

public enum InitializerConstants {
	TO("To:"), SUBJECT("Subject:"), BODY("Body:"), COMMA_DELIMITER(","), FROM("From:");

	private String value;

	private InitializerConstants(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}
}
