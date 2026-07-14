package com.echoworks.messaging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleMessageReader implements MessageReader{

	protected String messageFolder;
	
	private Path path  ;
	
	protected List<Message> messages;
	
	public SimpleMessageReader() {
		// TODO Auto-generated constructor stub
	}

	private void readFilesInFolder() {
		if(!path.toFile().exists()) 
			throw new MessageProcessorException("Folder does not exist");
		try (Stream<Path> paths = Files.walk(path)) {
		    messages =	paths.filter(Files::isRegularFile).map(f -> {
					return new Message(f.getParent() + "\\" + f.getFileName());				
			}).collect(Collectors.toList());
		}catch(IOException io) {
			throw new MessageProcessorException("Error processing file " + io);
		}
	}

	@Override
	public void readAndProcess(String folder) {
		this.messageFolder = folder;
		path = Paths.get(folder);
		readFilesInFolder();	
	}
	
	public Iterator<Message> iterator() {
		return Collections.unmodifiableList(messages).iterator();
	}
}
