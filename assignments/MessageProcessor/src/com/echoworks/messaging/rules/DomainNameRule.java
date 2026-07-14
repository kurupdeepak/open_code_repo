package com.echoworks.messaging.rules;

import com.echoworks.messaging.Message;

public class DomainNameRule implements MessageRule{
	static final String DOMAIN_TXT = "domain.com";
	
	@Override
	public boolean check(Message msg) {
		for(String e : msg.getToEmail()) {
			if(e.contains(DOMAIN_TXT))
				return true;
		}
		return false;
	}
}
