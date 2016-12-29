package com.lid.intellij.translateme.configuration;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

@State(
		name = ConfigurationComponent.COMPONENT_NAME,
		storages = {@Storage(id = "translate", file = "$PROJECT_FILE$")}
)
public final class ConfigurationComponent implements ProjectComponent, Configurable {
	public static final String COMPONENT_NAME = "Translate.ConfigurationComponent";
//  private final ImageIcon CONFIG_ICON =
//          helper.getIcon("resources/icon.png", getClass());

	public static final String CONFIGURATION_LOCATION;
	//+"/.IntelliJIdea70/config/inspection";

	static {
		CONFIGURATION_LOCATION = System.getProperty("user.home");
		//+"/.IntelliJIdea70/config/inspection";
	}

	private TranslationConfigurationForm form;

	@Override
	public boolean isModified() {
		return form != null && form.isModified(ConfigurationState.getInstance());
	}

	@Override
	public void projectOpened() {
		System.out.println("ConfigurationComponent.projectOpened");
	}

	@Override
	public void projectClosed() {
		System.out.println("ConfigurationComponent.projectClosed");
	}

	@NotNull
	public String getComponentName() {
		return COMPONENT_NAME;
	}

	@Override
	public void initComponent() {
	}

	@Override
	public void disposeComponent() {
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
			form.save(ConfigurationState.getInstance());
		}
	}

	/**
	 * Restores form values from configuration.
	 */
	@Override
	public void reset() {
		if (form != null) {
			form.load(ConfigurationState.getInstance());
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
