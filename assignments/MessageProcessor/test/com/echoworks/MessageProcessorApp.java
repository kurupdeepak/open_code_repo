package com.echoworks;

import java.util.Iterator;

import com.echoworks.messaging.Message;
import com.echoworks.messaging.MessageReader;
import com.echoworks.messaging.SimpleMessageReader;

public class MessageProcessorApp {

	public static void main(String[] args) {
		MessageReader rdr = new SimpleMessageReader();
		String baseDir = "C:\\quickworspace\\MessageProcessor\\test\\messages";
		String outDir = "C:\\quickworspace\\MessageProcessor\\test\\messages\\out";
		rdr.readAndProcess(baseDir);
		Iterator<Message> iter = ((SimpleMessageReader) rdr).iterator();
		System.out.println("Post processing the messages are as follows");
		while(iter.hasNext()) {
			System.out.println("<------------------start of message------------------------------>");
			Message msg = iter.next();
			System.out.println(msg);
			System.out.println("<------------------end of message------------------------------>");
		}
	}

}
