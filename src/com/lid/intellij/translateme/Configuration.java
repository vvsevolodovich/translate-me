package com.lid.intellij.translateme;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

@State(
		name = Configuration.COMPONENT_NAME,
		storages = {@Storage(id = "translate", file = "$PROJECT_FILE$")}
)
public final class Configuration implements ProjectComponent, Configurable, PersistentStateComponent<Configuration> {
	public static final String COMPONENT_NAME = "Translate.Configuration";
//  private final ImageIcon CONFIG_ICON =
//          helper.getIcon("resources/icon.png", getClass());

	public static final String CONFIGURATION_LOCATION;
	//+"/.IntelliJIdea70/config/inspection";

	static {
		CONFIGURATION_LOCATION = System.getProperty("user.home");
		//+"/.IntelliJIdea70/config/inspection";
	}

	private TranslationConfigurationForm form;
	private String langFrom = "en";
	private String langTo = "ru";

	public String getFrom() {
		return langFrom;
	}

	public String getTo() {
		return langTo;
	}

	public void setLangPair(final String from, final String to) {
		langFrom = from;
		langTo = to;
	}

	public boolean isModified() {
		return form != null && form.isModified(this);
	}

	public void projectOpened() {

	}

	public void projectClosed() {
	}

	@NotNull
	public String getComponentName() {
		return COMPONENT_NAME;
	}

	public void initComponent() {
	}

	public void disposeComponent() {
	}

	public String getDisplayName() {
		return "TranslateMe";
	}

	public Icon getIcon() {
//    return CONFIG_ICON;
		return null;
	}

	public String getHelpTopic() {
		return null;
	}

	public JComponent createComponent() {
		if (form == null) {
			form = new TranslationConfigurationForm();
		}

		return form.getRootComponent();
	}

	/**
	 * Stores settings from form to configuration bean.
	 *
	 * @throws ConfigurationException
	 */
	public void apply() throws ConfigurationException {
		if (form != null) {
			form.save(this);
		}
	}

	/**
	 * Restores form values from configuration.
	 */
	public void reset() {
		if (form != null) {
			form.load(this);
		}
	}

	/**
	 * Disposes UI resource.
	 */
	public void disposeUIResources() {
		form = null;
	}

	public Configuration getState() {
		return this;
	}

	public void loadState(Configuration state) {
		XmlSerializerUtil.copyBean(state, this);
	}

}
