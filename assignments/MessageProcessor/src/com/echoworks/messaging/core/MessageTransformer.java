package com.echoworks.messaging.core;

import com.echoworks.messaging.Message;

public interface MessageTransformer {
	void process();

	void process(Message message);
}
