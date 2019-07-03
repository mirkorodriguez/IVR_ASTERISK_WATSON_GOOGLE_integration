package com.parlana.core.rest.controller;

import static com.parlana.core.util.Constants.*;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.parlana.core.logic.dto.ClassifierDTO;
import com.parlana.core.logic.dto.EmotionDTO;
import com.parlana.core.logic.dto.SentimentDTO;
import com.parlana.core.logic.service.CallEventService;
import com.parlana.core.logic.service.ExtensionService;
import com.parlana.core.logic.service.impl.WatsonService;
import com.parlana.core.model.CallEvent;
import com.parlana.core.rest.request.dto.ProcessTextRequestDTO;
import com.parlana.core.rest.response.dto.ProcessTextResponseDTO;
import com.parlana.core.util.DateUtil;
import com.parlana.core.util.exception.BusinessException;


@CrossOrigin
@RestController
@RequestMapping("/s/text")
public class ProcessTextController {

	private static final Logger logger = LogManager.getLogger(ProcessTextController.class);

	@Autowired
	private CallEventService callEventService;

//	@Autowired
//	private ExtensionService extensionService;

	@RequestMapping(value = "/process", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ProcessTextResponseDTO processNLP(@RequestBody ProcessTextRequestDTO processTextRequestDTO) throws BusinessException {

		String callingNumber = processTextRequestDTO.getCallingNumber();
		String urlFile = processTextRequestDTO.getUrlFile();
		String text = processTextRequestDTO.getText();
		String confidence = processTextRequestDTO.getConfidence();

		logger.debug("Saving callingNumber: " + callingNumber);
		logger.debug("Saving urlFile: " + urlFile);
		logger.debug("Saving text: " + text);
		logger.debug("Saving confidence: " + confidence);

		ProcessTextResponseDTO processTextResponseDTO = new ProcessTextResponseDTO();

		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put("text", text);
		inputParams.put("callingNumber", callingNumber);

		ClassifierDTO classifierDTO = this.callClassifierService(inputParams);
		classifierDTO = this.populateData(classifierDTO);

		SentimentDTO sentimentDTO = this.callNluSentimentService(inputParams);

		EmotionDTO emotionDTO = this.callNluEmotion(inputParams);
		
		HashMap<String, String> allData = new HashMap<String, String>();
		
		// Populate allData
		DecimalFormat df = new DecimalFormat("#.####");
		allData.put("callEventDatetime", DateUtil.getCurrentDatetime());
		allData.put("callEventFrom", callingNumber);
		allData.put("callEventTo", "2000");
		allData.put("callEventToCode", "Central1");
		allData.put("callEventText", text);
		allData.put("callEventTextConfidence", df.format(Double.parseDouble(confidence)));
		allData.put("callEventAudioWav", urlFile);
		allData.put("callEventIntentCode", classifierDTO.getIntent());
		allData.put("callEventIntentConfidence", df.format(classifierDTO.getConfidence()));
		allData.put("callEventFinalExtension", classifierDTO.getExtensionNumber());
		allData.put("callEventFinalSentimentLabel", sentimentDTO.getSentimentLabel());
		allData.put("callEventFinalSentimentScore", df.format(sentimentDTO.getSentimentScore()));
		allData.put("callEventFinalEmotionSadness", String.valueOf(emotionDTO.getSadness()));
		allData.put("callEventFinalEmotionJoy", String.valueOf(emotionDTO.getJoy()));
		allData.put("callEventFinalEmotionFear", String.valueOf(emotionDTO.getFear()));
		allData.put("callEventFinalEmotionDisgust", String.valueOf(emotionDTO.getDisgust()));
		allData.put("callEventFinalEmotionAnger", String.valueOf(emotionDTO.getAnger()));
		this.saveAllDataInCallEvent(allData);

		processTextResponseDTO.setCallEventFinalExtension(allData.get("callEventFinalExtension"));
		processTextResponseDTO.setCallEventDatetime(allData.get("callEventDatetime"));
		processTextResponseDTO.setCallEventIntentCode(allData.get("callEventIntentCode"));
		processTextResponseDTO.setCallEventIntentConfidence(allData.get("callEventIntentConfidence"));
		processTextResponseDTO.setCallEventFinalSentimentLabel(allData.get("callEventFinalSentimentLabel"));
		processTextResponseDTO.setCallEventFinalSentimentScore(allData.get("callEventFinalSentimentScore"));
	 
		return processTextResponseDTO;
	}

	private ClassifierDTO callClassifierService(Map<String, Object> inputParams){
		 logger.info("Params : " + inputParams);
		 ClassifierDTO classifierDTO = WatsonService.watsonClassifier(inputParams);
		 logger.info("\n\nFINAL CLASSIFIER: " + classifierDTO.getIntent() +
		 "\nCONFIDENCE: " + classifierDTO.getConfidence() + "\n\n");
		 return classifierDTO;
	 }

	private ClassifierDTO populateData(ClassifierDTO classifierDTO) {
		//Obtain data for a specific Extension
//		Extension extension = extensionService.getExtensionByIntenName(classifierDTO.getIntent());
		String intent = classifierDTO.getIntent();
		Double confidence = classifierDTO.getConfidence();
		
		if(INTENT_FACTURACION.equals(intent)) {
			classifierDTO.setExtensionNumber("2001");
		}

		if(INTENT_CORTE_LINEA.equals(intent)) {
			classifierDTO.setExtensionNumber("2002");
		}
		
		if (confidence < INTENT_DEFAULT_CONFIDENCE) {
			classifierDTO.setIntent(INTENT_DEFAULT);
			classifierDTO.setExtensionNumber("DEFAULT");
		}

		return classifierDTO;
	}
	 
	private SentimentDTO callNluSentimentService(Map<String, Object> inputParams){
		 logger.info("Params : " + inputParams);
		 String text = (String)inputParams.get("text");
		 String callingNumber = (String)inputParams.get("callingNumber");
		 // Commented - Begin
		 SentimentDTO sentimentDTO = WatsonService.watsonNluSentiment(text,callingNumber);
		 logger.info("\n\nFINAL SENTIMENT: " + sentimentDTO.getSentimentLabel() + "\nSCORE: " + sentimentDTO.getSentimentScore() + "\n\n");
		 // Commented - End
		
		// SentimentDTO sentimentDTO = new SentimentDTO();
		// sentimentDTO.setSentimentLabel("default");
		// sentimentDTO.setSentimentScore(0.0);
		
		 return sentimentDTO;
	 }

	 private EmotionDTO callNluEmotion(Map<String, Object> inputParams) {
		 logger.info("Params : " + inputParams);
		 String text = (String)inputParams.get("text");
		 String callingNumber = (String)inputParams.get("callingNumber");
		 String textEN = WatsonService.watsonTranslator(text, LANG_BASE_ES, LANG_BASE_EN);
		 // Commented - Begin
		 EmotionDTO emotionDTO = WatsonService.watsonNluEmotion(textEN,callingNumber);
		 // Commented - End
		
		// EmotionDTO emotionDTO = new EmotionDTO();
		// emotionDTO.setSadness(0.0);
		// emotionDTO.setJoy(0.0);
		// emotionDTO.setFear(0.0);
		// emotionDTO.setDisgust(0.0);
		// emotionDTO.setAnger(0.0);
		 return emotionDTO;
	 }

		 private int saveAllDataInCallEvent(HashMap<String, String> allData) {
		
		 CallEvent callEvent = new CallEvent();
		
		 callEvent.setCallEventDatetime(allData.get("callEventDatetime"));
		 callEvent.setCallEventFrom(allData.get("callEventFrom"));
		 callEvent.setCallEventTo(allData.get("callEventTo"));
		 callEvent.setCallEventToCode(allData.get("callEventToCode"));
		 callEvent.setCallEventAudioWav(allData.get("callEventAudioWav"));
		 callEvent.setCallEventText(allData.get("callEventText"));
		 callEvent.setCallEventTextConfidence(allData.get("callEventTextConfidence"));
		 callEvent.setCallEventIntentCode(allData.get("callEventIntentCode"));
		 callEvent.setCallEventIntentConfidence(allData.get("callEventIntentConfidence"));
		 callEvent.setCallEventFinalExtension(allData.get("callEventFinalExtension"));
		 callEvent.setCallEventFinalSentimentLabel(allData.get("callEventFinalSentimentLabel"));
		 callEvent.setCallEventFinalSentimentScore(allData.get("callEventFinalSentimentScore"));
		 callEvent.setCallEventFinalEmotionSadness(allData.get("callEventFinalEmotionSadness"));
		 callEvent.setCallEventFinalEmotionJoy(allData.get("callEventFinalEmotionJoy"));
		 callEvent.setCallEventFinalEmotionFear(allData.get("callEventFinalEmotionFear"));
		 callEvent.setCallEventFinalEmotionDisgust(allData.get("callEventFinalEmotionDisgust"));
		 callEvent.setCallEventFinalEmotionAnger(allData.get("callEventFinalEmotionAnger"));
		
		 logger.debug("Registering call event FROM: " + callEvent.getCallEventFrom());
		 logger.debug("Registering call event TO: " + callEvent.getCallEventTo());
		
		 int insertResult = 0;
		 insertResult = callEventService.registerCallEvent(callEvent);
		 logger.info("insertResult: " + insertResult);
		 return insertResult;
		 }

}
