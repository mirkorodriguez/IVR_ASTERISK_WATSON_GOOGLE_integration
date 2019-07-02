package com.parlana.core.rest.request.dto;

public class ProcessTextRequestDTO {

	private String callingNumber;
	private String text;
	private String confidence;
	private String urlFile;
	
	
	public String getCallingNumber() {
		return callingNumber;
	}
	public void setCallingNumber(String callingNumber) {
		this.callingNumber = callingNumber;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getConfidence() {
		return confidence;
	}
	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}
	public String getUrlFile() {
		return urlFile;
	}
	public void setUrlFile(String urlFile) {
		this.urlFile = urlFile;
	}
	
	
}
