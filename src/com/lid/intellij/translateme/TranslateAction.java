package com.lid.intellij.translateme;

import com.google.gson.Gson;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import com.lid.intellij.translateme.yandex.DetectLanguageResponse;
import com.lid.intellij.translateme.yandex.TranslationResponse;
import com.lid.intellij.translateme.yandex.YandexClient;

import java.awt.*;
import java.util.List;

public class TranslateAction extends AnAction {

	@Override
	public void actionPerformed(AnActionEvent event) {
		Editor data = getEditor(event);
		if (data != null) {
			String selectedText = data.getSelectionModel().getSelectedText();
			if (selectedText != null && selectedText.length() > 0) {
				String splittedText = splitCamelCase(selectedText);
				splittedText = splitUnderscore(splittedText);

				Project project = event.getData(PlatformDataKeys.PROJECT);
				String[] langPairs = getLangPair(project);
				boolean autoDetect = isAutoDetect(project);
				String translated = translate(splittedText, langPairs, autoDetect);
				if (translated != null) {
					TranslationResponse response = new Gson().fromJson(translated, TranslationResponse.class);
					List<String> texts = response.getText();

					ResultDialog resultDialog = new ResultDialog("Translated", texts);
					resultDialog.setVisible(true);
				} else {
					showErrorBallon(event, "Failed to translate");
				}
			}
		}
	}

	private String translate(String splittedText, String[] langPairs, boolean autoDetect) {
		String translated;
		YandexClient yandexClient = new YandexClient();
		if (autoDetect) {
			String detect = yandexClient.detect(splittedText);
			DetectLanguageResponse response = new Gson().fromJson(detect, DetectLanguageResponse.class);
			int code = response.getCode();
			if (code == 200) {
				String language = response.getLang();
				translated = yandexClient.translate(splittedText, language, langPairs[1]);
			} else {
				// failed to detect language
				translated = yandexClient.translate(splittedText, langPairs[0], langPairs[1]);
			}
		} else {
			translated = yandexClient.translate(splittedText, langPairs[0], langPairs[1]);
		}
		return translated;
	}

	private void showErrorBallon(AnActionEvent event, String message) {
		BalloonBuilder builder =
				JBPopupFactory.getInstance().createHtmlTextBalloonBuilder("hello", MessageType.ERROR, null);
		Balloon balloon = builder.createBalloon();
		balloon.setTitle(message);
		Editor data = getEditor(event);
		CaretModel caretModel = data.getCaretModel();
		Point point = data.visualPositionToXY(caretModel.getVisualPosition());
		RelativePoint where = new RelativePoint(point);
		balloon.show(where, Balloon.Position.below);
	}

	private Editor getEditor(AnActionEvent event) {
		Project project = event.getData(PlatformDataKeys.PROJECT);
		return DataKeys.EDITOR.getData(event.getDataContext());
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

	public static boolean isAutoDetect(Project project) {
		if (project != null) {
			Configuration configuration = project.getComponent(Configuration.class);
			return configuration.isAutoDetect();
		}

		return false;
	}
}
