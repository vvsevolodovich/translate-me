package com.lid.intellij.translateme.configuration;

public class ConfigurationState {

	private String langFrom = "en";
	private String langTo = "ru";
	private boolean autoDetect = false;
	private boolean translationTooltip = false;
	private boolean splitCamelCase = false;
	private boolean splitUnderscores = false;

	public String getLangFrom() {
		return langFrom;
	}

	public String getLangTo() {
		return langTo;
	}

	// do not remove; necessary for settings serialization
	public void setLangFrom(String langFrom) {
		this.langFrom = langFrom;
	}

	// do not remove; necessary for settings serialization
	public void setLangTo(String langTo) {
		this.langTo = langTo;
	}

	public boolean isAutoDetect() {
		return autoDetect;
	}

	public void setAutoDetect(boolean autoDetect) {
		this.autoDetect = autoDetect;
	}

	public void setLangPair(final String from, final String to) {
		langFrom = from;
		langTo = to;
	}

	public boolean isTranslationTooltip() {
		return translationTooltip;
	}

	public void setTranslationTooltip(final boolean translationTooltip) {
		this.translationTooltip = translationTooltip;
	}

	public boolean isSplitCamelCase() {
		return splitCamelCase;
	}

	public void setSplitCamelCase(final boolean splitCamelCase) {
		this.splitCamelCase = splitCamelCase;
	}

	public boolean isSplitUnderscores() {
		return splitUnderscores;
	}

	public void setSplitUnderscores(final boolean splitUnderscores) {
		this.splitUnderscores = splitUnderscores;
	}
}
