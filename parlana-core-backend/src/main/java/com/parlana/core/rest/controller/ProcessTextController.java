package com.parlana.core.rest.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.parlana.core.logic.dto.ClassifierDTO;
import com.parlana.core.logic.dto.EmotionDTO;
import com.parlana.core.logic.dto.NlpRequestDTO;
import com.parlana.core.logic.dto.NlpResponseDTO;
import com.parlana.core.logic.dto.SentimentDTO;
import com.parlana.core.logic.dto.SpeechToTextDTO;
import com.parlana.core.logic.service.CallEventService;
import com.parlana.core.logic.service.CentralMasterService;
import com.parlana.core.logic.service.UtilService;
import com.parlana.core.logic.service.impl.GoogleService;
import com.parlana.core.logic.service.impl.WatsonService;
import com.parlana.core.model.CallEvent;
import com.parlana.core.model.CentralMaster;
import com.parlana.core.model.Extension;
import com.parlana.core.rest.request.dto.ProcessTextRequestDTO;
import com.parlana.core.rest.response.dto.ExtensionResponseDTO;
import com.parlana.core.rest.response.dto.ProcessTextResponseDTO;
import com.parlana.core.util.DateUtil;
import com.parlana.core.util.SystemUtil;
import com.parlana.core.util.exception.BusinessException;
import com.parlana.core.util.exception.CentralMasterNotFoundException;
import static com.parlana.core.util.Constants.*;

@CrossOrigin
@RestController
@RequestMapping("/s/text")
public class ProcessTextController {

	private static final Logger logger = LogManager.getLogger(ProcessTextController.class);

	@Autowired
	private CallEventService callEventService;

	@RequestMapping(value = "/process", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ProcessTextResponseDTO processNLP(@RequestBody ProcessTextRequestDTO processTextRequestDTO) throws BusinessException {

		String callingNumber = processTextRequestDTO.getCallingNumber();
		String urlFile = processTextRequestDTO.getUrlFile();
		String textES = processTextRequestDTO.getText();
		String confidence = processTextRequestDTO.getConfidence();

		logger.debug("Saving callingNumber: " + callingNumber);
		logger.debug("Saving urlFile: " + urlFile);
		logger.debug("Saving text: " + textES);
		logger.debug("Saving confidence: " + confidence);

		ProcessTextResponseDTO processTextResponseDTO = new ProcessTextResponseDTO();

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("textES", textES);

		String textEN = WatsonService.watsonTranslator(textES, LANG_BASE, "en");
		inputParams.put("textEE", textEN);
		inputParams.put("callingNumber", callingNumber);

		// ClassifierDTO classifierDTO = this.callClassifierService(inputParams);
		//
		 SentimentDTO sentimentDTO = this.callNluSentimentService(inputParams);
		
		 EmotionDTO emotionDTO = this.callNluEmotion(inputParams);
		//
		// HashMap<String, String> allData = new HashMap<String, String>();
		//
		// //Populate allData
		// DecimalFormat df = new DecimalFormat("#.####");
		// allData.put("callEventDatetime",DateUtil.getCurrentDatetime());
		// allData.put("callEventFrom",callingNumber);
		// allData.put("callEventTo",centralNumber);
		// allData.put("callEventToCode",centralCode);
		// allData.put("callEventText",speechToTextDTO.getTranscript());
		// allData.put("callEventTextConfidence",df.format(speechToTextDTO.getConfidence()));
		// allData.put("callEventAudioWav",filenameOnGoogleStorage);
		// allData.put("callEventIntentCode",classifierDTO.getIntent());
		// allData.put("callEventIntentConfidence",df.format(classifierDTO.getConfidence()));
		// allData.put("callEventFinalExtension",classifierDTO.getExtensionNumber());
		// allData.put("callEventFinalSentimentLabel",sentimentDTO.getSentimentLabel());
		// allData.put("callEventFinalSentimentScore",df.format(sentimentDTO.getSentimentScore()));
		// allData.put("callEventFinalEmotionSadness",String.valueOf(emotionDTO.getSadness()));
		// allData.put("callEventFinalEmotionJoy",String.valueOf(emotionDTO.getJoy()));
		// allData.put("callEventFinalEmotionFear",String.valueOf(emotionDTO.getFear()));
		// allData.put("callEventFinalEmotionDisgust",String.valueOf(emotionDTO.getDisgust()));
		// allData.put("callEventFinalEmotionAnger",String.valueOf(emotionDTO.getAnger()));
		//
		// //Others
		// allData.put("centralNumberPstn",centralNumberPstn);
		// allData.put("extensionAreaName",classifierDTO.getExtensionAreaName());
		// allData.put("extensionPersonEmail",classifierDTO.getExtensionPersonEmail());
		// allData.put("extensionPersonFullname",classifierDTO.getExtensionPersonFullname());
		// allData.put("extensionPersonPhone",classifierDTO.getExtensionPersonPhone());
		//
		// this.saveAllDataInCallEvent(allData);
		//
		// nlpResponseDTO.setCallEventFinalExtension(allData.get("callEventFinalExtension"));
		// nlpResponseDTO.setCallEventDatetime(allData.get("callEventDatetime"));
		// nlpResponseDTO.setCallEventIntentCode(allData.get("callEventIntentCode"));
		// nlpResponseDTO.setCallEventIntentConfidence(allData.get("callEventIntentConfidence"));
		// nlpResponseDTO.setCallEventFrom(allData.get("callEventFrom"));
		// nlpResponseDTO.setCallEventTo(allData.get("callEventTo"));
		// nlpResponseDTO.setCallEventToCode(allData.get("callEventToCode"));
		// nlpResponseDTO.setCentralNumberPstn(allData.get("centralNumberPstn"));
		// nlpResponseDTO.setCallEventText(allData.get("callEventText"));
		// nlpResponseDTO.setCallEventFinalSentimentLabel(allData.get("callEventFinalSentimentLabel"));
		// nlpResponseDTO.setCallEventFinalSentimentScore(allData.get("callEventFinalSentimentScore"));
		//
		// nlpResponseDTO.setExtensionPersonEmail(allData.get("extensionPersonEmail"));
		// nlpResponseDTO.setExtensionPersonPhone(allData.get("extensionPersonPhone"));
		//
		// return nlpResponseDTO;
		// }

		// private ClassifierDTO callClassifierService(Map<String, Object> inputParams)
		// {
		// logger.info("Params : " + inputParams);
		// ClassifierDTO classifierDTO = WatsonService.watsonClassifier(inputParams);
		// logger.info("\n\nFINAL CLASSIFIER: " + classifierDTO.getIntent() +
		// "\nCONFIDENCE: " + classifierDTO.getConfidence() + "\n\n");
		// return classifierDTO;
		// }
		 
		return processTextResponseDTO;
	}
		 
	private SentimentDTO callNluSentimentService(Map<String, Object> inputParams)
	 {
	 logger.info("Params : " + inputParams);
	 String speechToText = (String)inputParams.get("speechToText");
	 String callingNumber = (String)inputParams.get("callingNumber");
	 // Commented - Begin
	 SentimentDTO sentimentDTO = WatsonService.watsonNluSentiment(speechToText,
	 callingNumber);
	 logger.info("\n\nFINAL SENTIMENT: " + sentimentDTO.getSentimentLabel() +
	 "\nSCORE: " + sentimentDTO.getSentimentScore() + "\n\n");
	 // Commented - End
	
	// SentimentDTO sentimentDTO = new SentimentDTO();
	// sentimentDTO.setSentimentLabel("default");
	// sentimentDTO.setSentimentScore(0.0);
	
	 return sentimentDTO;
	 }

	 private EmotionDTO callNluEmotion(Map<String, Object> inputParams) {
	 logger.info("Params : " + inputParams);
	 String speechToText = (String)inputParams.get("speechToText");
	 String speechToTextOriginal =
	 (String)inputParams.get("speechToTextOriginal");
	 String callingNumber = (String)inputParams.get("callingNumber");
	 String langEvaluated = (String)inputParams.get("langEvaluated");
	 System.out.println("langEvaluated: " + langEvaluated);
	 String speechToTextEN =
	 WatsonService.watsonTranslator(speechToTextOriginal,langEvaluated,"en");
	
	 // Commented - Begin
	 EmotionDTO emotionDTO = WatsonService.watsonNluEmotion(speechToTextEN,
	 callingNumber);
	 // Commented - End
	
	// EmotionDTO emotionDTO = new EmotionDTO();
	// emotionDTO.setSadness(0.0);
	// emotionDTO.setJoy(0.0);
	// emotionDTO.setFear(0.0);
	// emotionDTO.setDisgust(0.0);
	// emotionDTO.setAnger(0.0);
	 return emotionDTO;
	 }

		// private int saveAllDataInCallEvent(HashMap<String, String> allData) {
		//
		// CallEvent callEvent = new CallEvent();
		//
		// callEvent.setCallEventDatetime(allData.get("callEventDatetime"));
		// callEvent.setCallEventFrom(allData.get("callEventFrom"));
		// callEvent.setCallEventTo(allData.get("callEventTo"));
		// callEvent.setCallEventToCode(allData.get("callEventToCode"));
		// callEvent.setCallEventAudioWav(allData.get("callEventAudioWav"));
		// callEvent.setCallEventText(allData.get("callEventText"));
		// callEvent.setCallEventTextConfidence(allData.get("callEventTextConfidence"));
		// callEvent.setCallEventIntentCode(allData.get("callEventIntentCode"));
		// callEvent.setCallEventIntentConfidence(allData.get("callEventIntentConfidence"));
		// callEvent.setCallEventFinalExtension(allData.get("callEventFinalExtension"));
		// callEvent.setCallEventFinalSentimentLabel(allData.get("callEventFinalSentimentLabel"));
		// callEvent.setCallEventFinalSentimentScore(allData.get("callEventFinalSentimentScore"));
		// callEvent.setCallEventFinalEmotionSadness(allData.get("callEventFinalEmotionSadness"));
		// callEvent.setCallEventFinalEmotionJoy(allData.get("callEventFinalEmotionJoy"));
		// callEvent.setCallEventFinalEmotionFear(allData.get("callEventFinalEmotionFear"));
		// callEvent.setCallEventFinalEmotionDisgust(allData.get("callEventFinalEmotionDisgust"));
		// callEvent.setCallEventFinalEmotionAnger(allData.get("callEventFinalEmotionAnger"));
		//
		// logger.debug("Registering call event FROM: " + callEvent.getCallEventFrom());
		// logger.debug("Registering call event TO: " + callEvent.getCallEventTo());
		//
		// int insertResult = 0;
		// insertResult = callEventService.registerCallEvent(callEvent);
		// logger.info("insertResult: " + insertResult);
		// return insertResult;
		// }

}
