package com.echoworks.messaging.rules;

import com.echoworks.messaging.Message;

public interface MessageRule {

	boolean check(Message msg);
}
