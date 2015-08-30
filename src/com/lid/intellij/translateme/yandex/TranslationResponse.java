package com.lid.intellij.translateme.yandex;

import java.util.List;

public class TranslationResponse {

	int code;
	String lang;
	List<String> text;

	public int getCode() {
		return code;
	}

	public String getLang() {
		return lang;
	}

	public List<String> getText() {
		return text;
	}
}
