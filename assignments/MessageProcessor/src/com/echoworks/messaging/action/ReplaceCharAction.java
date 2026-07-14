package com.echoworks.messaging.action;

import com.echoworks.messaging.Message;

public class ReplaceCharAction implements Action {
	@Override
	public void apply(Message msg) {
		msg.setBody(msg.getBody().
				replace('$', 'e').
				replace('^', 'y').
				replace('&', 'u'));
	}
}
