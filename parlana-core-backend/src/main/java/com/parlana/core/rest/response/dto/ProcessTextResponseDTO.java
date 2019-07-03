package com.parlana.core.rest.response.dto;

public class ProcessTextResponseDTO {

	private String callEventDatetime;
	private String callEventFinalExtension;
	private String callEventIntentCode;
	private String callEventIntentConfidence;
	private String callEventFinalSentimentLabel;
	private String callEventFinalSentimentScore;
    
	public String getCallEventFinalExtension() {
		return callEventFinalExtension;
	}
	public void setCallEventFinalExtension(String callEventFinalExtension) {
		this.callEventFinalExtension = callEventFinalExtension;
	}
	public String getCallEventDatetime() {
		return callEventDatetime;
	}
	public void setCallEventDatetime(String callEventDatetime) {
		this.callEventDatetime = callEventDatetime;
	}
	public String getCallEventIntentCode() {
		return callEventIntentCode;
	}
	public void setCallEventIntentCode(String callEventIntentCode) {
		this.callEventIntentCode = callEventIntentCode;
	}
	public String getCallEventIntentConfidence() {
		return callEventIntentConfidence;
	}
	public void setCallEventIntentConfidence(String callEventIntentConfidence) {
		this.callEventIntentConfidence = callEventIntentConfidence;
	}
	public String getCallEventFinalSentimentLabel() {
		return callEventFinalSentimentLabel;
	}
	public void setCallEventFinalSentimentLabel(String callEventFinalSentimentLabel) {
		this.callEventFinalSentimentLabel = callEventFinalSentimentLabel;
	}
	public String getCallEventFinalSentimentScore() {
		return callEventFinalSentimentScore;
	}
	public void setCallEventFinalSentimentScore(String callEventFinalSentimentScore) {
		this.callEventFinalSentimentScore = callEventFinalSentimentScore;
	}
	
}
