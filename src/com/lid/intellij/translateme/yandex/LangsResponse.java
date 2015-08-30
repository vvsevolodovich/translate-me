package com.lid.intellij.translateme.yandex;


import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * {"dirs":
 ["ru-en","ru-pl","ru-uk","ru-de","ru-fr","ru-es","ru-it","ru-bg","ru-cs","ru-tr","ru-ro","ru-sr","en-ru","en-uk","en-de","en-fr","en-es","en-it","en-cs","en-tr","pl-ru","pl-uk","uk-ru","uk-en","uk-pl","uk-de","uk-fr","uk-es","uk-it","uk-bg","uk-cs","uk-tr","uk-ro","uk-sr","de-ru","de-en","de-uk","fr-ru","fr-en","fr-uk","es-ru","es-en","es-uk","it-ru","it-en","it-uk","bg-ru","bg-uk","cs-ru","cs-en","cs-uk","tr-ru","tr-en","tr-uk","ro-ru","ro-uk","sr-ru","sr-uk"],
 "langs":{"ru":"російська","en":"англійська","pl":"польська","uk":"українська","de":"німецька","fr":"французька","es":"іспанська","it":"італійська","bg":"болгарська","cs":"чеська","tr":"турецька","ro":"румунська","sr":"сербська"}
 }
 */
public class LangsResponse {

	@SerializedName("dirs")
	List<String> pairs;

	@SerializedName("langs")
	Map<String, String> langs;

	public List<String> getPairs() {
		return pairs;
	}

	public Map<String, String> getLangs() {
		return langs;
	}
}
