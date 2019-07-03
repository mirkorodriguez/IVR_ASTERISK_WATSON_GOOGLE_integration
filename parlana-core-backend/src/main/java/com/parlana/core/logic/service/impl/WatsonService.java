package com.parlana.core.logic.service.impl;


import static com.parlana.core.util.Constants.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.parlana.core.logic.dto.ClassifierDTO;
import com.parlana.core.logic.dto.EmotionDTO;
import com.parlana.core.logic.dto.SentimentDTO;
import com.parlana.core.rest.response.dto.ExtensionResponseDTO;
import com.parlana.core.util.SystemUtil;


public class WatsonService {
	
	private static final Logger logger = LogManager.getLogger(WatsonService.class);
	
	public static ClassifierDTO watsonClassifier(Map<String,Object> params){
		
		String text = (String) params.get("text");
	    
  	    String[] command = {"curl","-G","-u","apikey:" + WATSON_NLC_KEY, WATSON_NLC_ENDPOINT, "--data-urlencode","text=" + text};
  	
		String result = SystemUtil.processCommand(command);
		JSONObject object = new JSONObject(result);
		logger.info("Result: \n"+object);
		
	    JSONArray classesJson = object.getJSONArray("classes");
	    String top_class = object.getString("top_class");
	    logger.info("\nclassesJson: " + classesJson);
	    logger.info("\ntop_class: "+top_class);
	    
    	String intent = INTENT_DEFAULT;
	    double confidence = 0.0;

	    ClassifierDTO classifierDTO = new ClassifierDTO();
    	classifierDTO.setIntent(intent);
	    classifierDTO.setConfidence(confidence);

	    for (int i = 0; i < classesJson.length(); i++) {
	    	intent = classesJson.getJSONObject(i).getString("class_name");
	    	confidence = classesJson.getJSONObject(i).getDouble("confidence");
			if(top_class.equals(intent)) {
				classifierDTO.setIntent(intent);
			    classifierDTO.setConfidence(confidence);
			    break;
			}
		}
	    
		return classifierDTO;
	}
	
	public static SentimentDTO watsonNluSentiment(String text, String handle){
		String filename = HOME_RESOURCES_WATSON + "watson_input_nlu_sentiment.json";
		String newFilename = HOME_RESOURCES_WATSON + "watson_input_nlu_sentiment_" + handle + ".json";
	    try {
	    	SystemUtil.copyFile(new File(filename),new File(newFilename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    if(new File(newFilename).exists()) {
	    	SystemUtil.replaceTextInFile(new File(newFilename), "REPLACEME", text);	
	    }
		
		String[] command = {"curl","-X","POST","-u","apikey:" + WATSON_NLU_KEY,"--header","Content-Type: application/json","--data","@"+newFilename,WATSON_NLU_ENDPOINT};
		
		String result = SystemUtil.processCommand(command);
		JSONObject object = new JSONObject(result);
		logger.info("Result: \n " + object);
	    JSONObject sentiment = object.getJSONObject("sentiment");
	    JSONObject document = sentiment.getJSONObject("document");
	    Double sentimentScore = document.getDouble("score");
	    String sentimentLabel = document.getString("label");
	    SentimentDTO sentimentDTO = new SentimentDTO();
	    sentimentDTO.setSentimentScore(sentimentScore);
	    sentimentDTO.setSentimentLabel(sentimentLabel);
	    (new File(newFilename)).delete();
	    return sentimentDTO;
	}
	
	public static EmotionDTO watsonNluEmotion(String text, String handle){
		String filename = HOME_RESOURCES_WATSON + "watson_input_nlu_emotion.json";
		String newFilename = HOME_RESOURCES_WATSON + "watson_input_nlu_emotion_" + handle + ".json";
		
	    try {
	    	SystemUtil.copyFile(new File(filename),new File(newFilename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    if(new File(newFilename).exists()) {
	    	SystemUtil.replaceTextInFile(new File(newFilename), "REPLACEME", text);	
	    }
		
	    String[] command = {"curl","-X","POST","-u","apikey:" + WATSON_NLU_KEY,"--header","Content-Type: application/json","--data","@"+newFilename,WATSON_NLU_ENDPOINT};
		
		String result = SystemUtil.processCommand(command);
		JSONObject object = new JSONObject(result);
		logger.info("Result: \n" + object);
		
	    JSONObject emotion = object.getJSONObject("emotion");
	    JSONObject document = emotion.getJSONObject("document");
	    JSONObject emotionDetail = document.getJSONObject("emotion");

	    Double sadness = emotionDetail.getDouble("sadness");
	    Double joy = emotionDetail.getDouble("joy");
	    Double fear = emotionDetail.getDouble("fear");
	    Double disgust = emotionDetail.getDouble("disgust");
	    Double anger = emotionDetail.getDouble("anger");
	    
	    EmotionDTO emotionDTO = new EmotionDTO();
	    emotionDTO.setSadness(sadness);
	    emotionDTO.setJoy(joy);
	    emotionDTO.setFear(fear);
	    emotionDTO.setDisgust(disgust);
	    emotionDTO.setAnger(anger);
	    
	    logger.info("--> TRISTEZA: " + sadness);
	    logger.info("--> ALEGRIA: " + joy);
	    logger.info("--> MIEDO: " + fear);
	    logger.info("--> DISGUSTO: " + disgust);
	    logger.info("--> ENFADO: " + anger);
	    
	    (new File(newFilename)).delete();
	    return emotionDTO;
	}

	public static String watsonTranslator(String text, String source, String target){
		String modelId = source + "-" + target;
		String[] command = {"curl","-X","POST","-u","apikey:" + WATSON_TRANSLATOR_KEY,"--header","Content-Type: application/json","--data","{ \"text\": \"" + text + "\", \"model_id\" : \"" + modelId + "\"}", WATSON_TRANSLATOR_ENDPOINT};
	
		String result = SystemUtil.processCommand(command);
		JSONObject object = new JSONObject(result);
		logger.info("Result: \n" + object);
		
		JSONArray translations = object.getJSONArray("translations");
		
	    return translations.getJSONObject(0).getString("translation");
	}

}
