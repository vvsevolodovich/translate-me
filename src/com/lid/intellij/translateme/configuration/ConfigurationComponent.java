package com.lid.intellij.translateme.configuration;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class ConfigurationComponent implements Configurable {
	public static final String COMPONENT_NAME = "Translate.ConfigurationComponent";
//  private final ImageIcon CONFIG_ICON =
//          helper.getIcon("resources/icon.png", getClass());

	//public static final String CONFIGURATION_LOCATION;
	//+"/.IntelliJIdea70/config/inspection";

	//static {
	//	CONFIGURATION_LOCATION = System.getProperty("user.home");
	//	//+"/.IntelliJIdea70/config/inspection";
	//}

	private TranslationConfigurationForm form;
	private PersistingService instance;

	@Override
	public boolean isModified() {
		return form != null && form.isModified(getState());
	}


	private ConfigurationState getState() {
		return instance.getState();
	}

	@NotNull
	public String getComponentName() {
		return COMPONENT_NAME;
	}

	@Override
	public String getDisplayName() {
		return "TranslateMe";
	}

	public Icon getIcon() {
//    return CONFIG_ICON;
		return null;
	}

	@Override
	public String getHelpTopic() {
		return null;
	}

	@Override
	public JComponent createComponent() {
		if (form == null) {
			form = new TranslationConfigurationForm();
		}
		instance = PersistingService.getInstance();
		ConfigurationState state = instance.getState();
		form.load(state);

		return form.getRootComponent();
	}

	/**
	 * Stores settings from form to configuration bean.
	 *
	 * @throws ConfigurationException
	 */
	@Override
	public void apply() throws ConfigurationException {
		if (form != null) {
			form.save(getState());
		}
	}

	/**
	 * Restores form values from configuration.
	 */
	@Override
	public void reset() {
		if (form != null) {
			form.load(getState());
		}
	}

	/**
	 * Disposes UI resource.
	 */
	@Override
	public void disposeUIResources() {
		form = null;
	}

}
