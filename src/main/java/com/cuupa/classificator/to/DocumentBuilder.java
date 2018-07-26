package com.cuupa.classificator.to;

import java.io.IOException;
import java.io.InputStream;

public class DocumentBuilder {

	private final InputStream inputStream;
	private final String filename;
	private final int fileSize;

	private DocumentBuilder(InputStream inputStream, String filename, int fileSize) {
		this.inputStream = inputStream;
		this.filename = filename;
		this.fileSize = fileSize;
	}

	public static DocumentBuilder create(InputStream inputStream, String fileName, int fileSize) {
		return new DocumentBuilder(inputStream, fileName, fileSize);
	}

	public Document build() throws IOException {
		Document document = new Document();
		document.setFileName(filename);
		byte[] content = new byte[fileSize];
		inputStream.read(content);
		document.setContent(content);
		return document;
	}

}
