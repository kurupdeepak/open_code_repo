package com.echoworks.messaging.action;

import com.echoworks.messaging.Message;

public interface Action {
	void apply(Message msg);
}
