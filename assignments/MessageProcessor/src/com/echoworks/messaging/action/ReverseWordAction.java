package com.echoworks.messaging.action;

import com.echoworks.messaging.Message;

public class ReverseWordAction implements Action{
	static final String WHITE_SPACE = " ";
	@Override
	public void apply(Message msg) {
		String str [] = msg.getBody().split(WHITE_SPACE);
		StringBuilder res = new StringBuilder();
		for(String s : str) {
			res.append(new StringBuilder(s).reverse()).append(" ");
		}
		msg.setBody(res.toString());
	}

}
