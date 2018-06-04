package com.lid.intellij.translateme.translators;


import com.google.gson.Gson;
import com.lid.intellij.translateme.yandex.DetectLanguageResponse;
import com.lid.intellij.translateme.yandex.TranslationResponse;
import com.lid.intellij.translateme.yandex.YandexClient;

import java.util.Collections;
import java.util.List;

public class YandexTranslator implements Translator {

	@Override
	public List<String> translate(String text, String[] langPair, boolean autoDetect) {
		return translate0(text, langPair, autoDetect);
	}

	private List<String> translate0(String splittedText, String[] langPairs, boolean autoDetect) {
		String translated;
		YandexClient yandexClient = new YandexClient();
		if (autoDetect) {
			String detect = yandexClient.detect(splittedText, langPairs[0], langPairs[1]);
			DetectLanguageResponse response = new Gson().fromJson(detect, DetectLanguageResponse.class);
			if (response != null && response.getCode() == 200) {
				String language = response.getLang();
				translated = yandexClient.translate(splittedText, language, langPairs[1]);
			} else {
				// failed to detect language
				translated = yandexClient.translate(splittedText, langPairs[0], langPairs[1]);
			}
		} else {
			translated = yandexClient.translate(splittedText, langPairs[0], langPairs[1]);
		}

		if (translated != null) {
			TranslationResponse response = new Gson().fromJson(translated, TranslationResponse.class);
			return response.getText();
		}
		return Collections.emptyList();
	}

}
