package com.echoworks.messaging.core;

import java.io.FileNotFoundException;

import com.echoworks.messaging.MessageProcessorException;

public interface MessageInitializer {
	void init() throws MessageProcessorException;
}
