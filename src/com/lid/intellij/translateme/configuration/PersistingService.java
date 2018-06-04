package com.lid.intellij.translateme.configuration;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "translateMe",
	storages = @Storage("translateMe.xml")
)
public class PersistingService implements PersistentStateComponent<ConfigurationState> {
	private ConfigurationState state;

	public static PersistingService getInstance() 	{
		return ServiceManager.getService(PersistingService.class);
	}

	@Nullable
	@Override
	public ConfigurationState getState() {
		return state;
	}

	@Override
	public void loadState(@NotNull ConfigurationState configurationState) {
		state = configurationState;
	}

	@Override
	public void noStateLoaded() {
		state = new ConfigurationState();
		state.setLangPair("en", "ru");
		state.setAutoDetect(false);
	}
}
