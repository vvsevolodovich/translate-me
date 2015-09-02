package com.lid.intellij.translateme;

import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.lid.intellij.translateme.yandex.TranslationResponse;
import com.lid.intellij.translateme.yandex.YandexClient;

import java.util.List;

public class TranslateAction extends AnAction {

	@Override
	public void actionPerformed(AnActionEvent event) {
		Project project = event.getData(PlatformDataKeys.PROJECT);
		Editor data = DataKeys.EDITOR.getData(event.getDataContext());
		if (data != null) {
			String selectedText = data.getSelectionModel().getSelectedText();
			if (selectedText != null && selectedText.length() > 0) {
				String splittedText = splitCamelCase(selectedText);
				splittedText = splitUnderscore(splittedText);
				String[] langPairs = getLangPair(project);
				String translated = new YandexClient().translate(splittedText, langPairs[0], langPairs[1]);
				TranslationResponse response = new Gson().fromJson(translated, TranslationResponse.class);
				List<String> texts = response.getText();

				ResultDialog resultDialog = new ResultDialog("Translated", texts);
				resultDialog.setVisible(true);
			}
		}
	}

	private String splitUnderscore(String splittedText) {
		String[] splitted = splittedText.split("_");
		return arrayToString(splitted);
	}

	private String splitCamelCase(String selectedText) {
		String[] splitted = selectedText.split("(?<=[a-z])(?=[A-Z])");
		return arrayToString(splitted);
	}

	private String arrayToString(String[] splitted) {
		if (splitted.length == 1) {
			return splitted[0];
		}
		StringBuilder builder = new StringBuilder();
		for (String word : splitted) {
			builder.append(word).append(" ");
		}
		return builder.toString();
	}

	public static String[] getLangPair(Project project) {

		if (project != null) {
			Configuration configuration = project.getComponent(Configuration.class);

			String from = configuration.getFrom();
			String to = configuration.getTo();
			return new String[]{from, to};
		}

		return new String[]{"en", "ru"};
	}
}
