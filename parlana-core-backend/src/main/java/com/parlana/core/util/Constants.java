package com.parlana.core.util;

public class Constants {
	
//  Make google bucket readable to download audio files	
//	$ ./gsutil iam ch allUsers:objectViewer gs://mirkos-bucket

	// Local
//	public final static String HOME_RESOURCES_FILES = "/home/mirko/Desktop/Tmp/tmp2/git-repos/IVR_ASTERISK_WATSON_GOOGLE_integration/parlana-core-backend/src/main/resources/files/";
	// Cloud
	public final static String HOME_RESOURCES_FILES = "/home/watson_ibm_peru/apache-tomee-plume-8.0.0-M3/webapps/parlana-core-backend/WEB-INF/classes/files/";

	public final static String KEY = "@vay@";

	public final static String GOOGLE_APPLICATION_CREDENTIALS = "GOOGLE_APPLICATION_CREDENTIALS";
	public final static String HOME_RESOURCES_WATSON = HOME_RESOURCES_FILES + "watson/";
	public final static String HOME_RESOURCES_GOOGLE = HOME_RESOURCES_FILES + "google/";

	public final static String INTENT_DEFAULT = "INTENT_DEFAULT";
	public final static Double INTENT_DEFAULT_CONFIDENCE = 0.95;
	public final static String INTENT_CORTE_LINEA = "INTENT_CORTE_LINEA";
	public final static String INTENT_FACTURACION = "INTENT_FACTURACION";

	public final static String LANG_BASE_ES = "es";
	public final static String LANG_BASE_EN = "en";
	
	// Google Cloud
	public final static String GOOGLE_SPEECH_API_URI = "https://speech.googleapis.com/v1/speech:recognize";
	public final static String GOOGLE_STORAGE_URI = "https://storage.googleapis.com/mirkos-bucket/";
	public final static String GOOGLE_STORAGE_GS_URI = "gs://mirkos-bucket/";

	// Watson Conversation
	public final static String WATSON_CONV_WORKSPACE_ID = "c63c58b3-27f6-4f05-a48d-376371974af9";
	public final static String WATSON_CONV_ENDPOINT = "https://gateway.watsonplatform.net/natural-language-classifier/api/v1/classifiers/773d84x567-nlc-915/classify";
	public final static String WATSON_CONV_USERNAME = "a9ef5682-f265-4e11-972a-058fee624166";
	public final static String WATSON_CONV_PASSWORD = "QNHJWrnm7lVH";

	// Watson NLC
	public final static String WATSON_NLC_CLASSIFIER_ID = "b50852x572-nlc-80";
	public final static String WATSON_NLC_ENDPOINT = "https://gateway.watsonplatform.net/natural-language-classifier/api/v1/classifiers/" + WATSON_NLC_CLASSIFIER_ID + "/classify";
	public final static String WATSON_NLC_KEY = "oPBZPeCKPGxhCTwGyBgVTn-cdkP1ij6WbMLpyjlgYF9w";

	// Watson NLU
	public final static String WATSON_NLU_ENDPOINT = "https://gateway.watsonplatform.net/natural-language-understanding/api/v1/analyze?version=2018-11-16";
	public final static String WATSON_NLU_KEY = "ZYbqM_XVTAjKWD6EQCitVFMqpMTpCK3R1cWh1tzuhyqh";

	// Watson Translator
	public final static String WATSON_TRANSLATOR_ENDPOINT = "https://gateway.watsonplatform.net/language-translator/api/v3/translate?version=2018-05-01";
	public final static String WATSON_TRANSLATOR_KEY = "LcmgT5bignKqQNbzM1YJ5vX3r33kv5xdrgCK9AwKOxE4";

}
