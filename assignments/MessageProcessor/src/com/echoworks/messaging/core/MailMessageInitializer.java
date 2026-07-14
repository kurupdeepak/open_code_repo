package com.echoworks.messaging.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;

import com.echoworks.messaging.InitializerConstants;
import com.echoworks.messaging.Message;
import com.echoworks.messaging.MessageProcessorException;

public class MailMessageInitializer implements MessageInitializer {
	Message message ; 
	
	enum MessagePartIndex {
		TO_LINE(1), FROM_LINE(2), SUB_LINE(3), BODY_LINE(4);

		int n;
		

		MessagePartIndex(int n) {
			this.n = n;
		}
		
		int getIndex() {
			return n;
		}
	}
	
	public MailMessageInitializer(Message msg) {
		this.message = msg;
	}
	
	private void processMessage() throws FileNotFoundException {
		File file = Paths.get(message.getFileName()).toFile();
		try (Scanner scanner = new Scanner(file).useDelimiter(System.lineSeparator())) {
			int index = 1;
			while (scanner.hasNextLine()) {
				if (index == MessagePartIndex.TO_LINE.getIndex())
					initializeTo(scanner.nextLine());
				if (index == MessagePartIndex.FROM_LINE.getIndex())
					initializeFrom(scanner.nextLine());
				if (index == MessagePartIndex.SUB_LINE.getIndex())
					initializeSubject(scanner.nextLine());
				if (index == MessagePartIndex.BODY_LINE.getIndex())
					initializeBody(scanner);
				index++;
			}
		}
		
	}
	
	private void initializeBody(Scanner s) {
		StringBuilder sb = new StringBuilder();
		while(s.hasNextLine()) {
			String val = s.nextLine();
			if(val.contains(InitializerConstants.BODY.value()))
				continue;
			sb.append(val).
			append(System.lineSeparator());
		}
		message.setBody(sb.toString());
	}

	private void initializeSubject(String line) {
		try(Scanner s = new Scanner(line.substring(InitializerConstants.SUBJECT.value().length()))) {
			if(s.hasNextLine()) {
				message.setSubject(s.nextLine());
			}
		}
	}

	private void initializeFrom(String line) {
		try(Scanner s = new Scanner(line.substring(InitializerConstants.FROM.value().length()))) {
			if(s.hasNext()) {
				message.setFromEmail(s.next());
			}
		}
	}

	private void initializeTo(String line) {
		try(Scanner s = new Scanner(line.substring(InitializerConstants.TO.value().length())).useDelimiter(InitializerConstants.COMMA_DELIMITER.value())){
			while(s.hasNext()) {
				String val = s.next();
				message.getToEmail().add(val);
			}
		}
	}

	@Override
	public void init() {
		try {
			processMessage();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MessageProcessorException("Failed to read file " + message.getFileName() + " error " + e);
		}
		
	}

}
