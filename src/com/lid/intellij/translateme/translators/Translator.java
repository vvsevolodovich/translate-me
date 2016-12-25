package com.lid.intellij.translateme.translators;

import java.util.List;

public interface Translator {

	public List<String> translate(String text, String[] langPair, boolean autoDetect);
}
