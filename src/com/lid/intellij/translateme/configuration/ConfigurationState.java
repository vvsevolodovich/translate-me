package com.lid.intellij.translateme.configuration;


import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;

@State(
		name = ConfigurationComponent.COMPONENT_NAME,
		storages = {@Storage(id = "translate", file = "translateMe.xml")}
)
public class ConfigurationState implements PersistentStateComponent<ConfigurationState> {

	private String langFrom = "en";
	private String langTo = "ru";
	private boolean autoDetect = false;

	public static ConfigurationState getInstance() {
		return ServiceManager.getService(ConfigurationState.class);
	}

	public String getFrom() {
		return langFrom;
	}

	public String getTo() {
		return langTo;
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

	@Override
	public ConfigurationState getState() {
		return this;
	}

	@Override
	public void loadState(ConfigurationState state) {
		XmlSerializerUtil.copyBean(state, this);
	}
}
