package com.echoworks.messaging.rules;

import com.echoworks.messaging.Message;

public class SecureTxtRule implements MessageRule{
	static final String SECURE_TXT = "SECURE:";
	
	@Override
	public boolean check(Message msg) {
		return msg.getSubject().startsWith(SECURE_TXT);
	}
	
	
	
}
