package com.cuupa.classificator.to;


import java.io.Serializable;

public class Document implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private byte[] content;
	
	private String filename;

	private boolean encrypted;

	public void setContent(byte[] content) {
		this.content = content;
	}

	public void setFileName(String filename) {
		this.filename = filename;
	}

	public byte[] getContent() {
		return content;
	}

	public String getFileName() {
		return filename;
	}

	public void setEncrypted(boolean encrypted) {
		this.encrypted = encrypted;
	}

	public boolean isEncrypted() {
		return encrypted;
	}

}
