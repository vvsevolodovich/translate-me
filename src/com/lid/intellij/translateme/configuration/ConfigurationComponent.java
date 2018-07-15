package com.lid.intellij.translateme.configuration;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;

import javax.swing.*;

public final class ConfigurationComponent implements Configurable {

	private TranslationConfigurationForm form;
	private PersistingService instance;

	@Override
	public boolean isModified() {
		return form != null && form.isModified(getState());
	}


	private ConfigurationState getState() {
		return instance.getState();
	}

	@Override
	public String getDisplayName() {
		return "TranslateMe";
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
	 */
	@Override
	public void apply() {
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
