package com.lid.intellij.translateme.actions;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.project.Project;
import com.lid.intellij.translateme.configuration.ConfigurationState;
import com.lid.intellij.translateme.configuration.PersistingService;
import com.lid.intellij.translateme.translators.YandexTranslator;
import org.jetbrains.annotations.Nullable;

import java.util.List;

class TranslateHandler extends EditorWriteActionHandler {

	private final ActionHandler mHandler;

	public TranslateHandler(ActionHandler handler) {
		this.mHandler = handler;
	}

	@Override
	public void executeWriteAction(Editor editor, @Nullable Caret caret, DataContext dataContext) {
		if (editor == null) {
			return;
		}

		Project project = editor.getProject();
		ConfigurationState state = PersistingService.getInstance().getState();

		String selectedText = editor.getSelectionModel().getSelectedText();
		if (selectedText != null && selectedText.length() > 0) {
			String splittedText = state.isSplitCamelCase() ? splitCamelCase(selectedText) : selectedText;
			splittedText = state.isSplitUnderscores() ? splitUnderscore(splittedText) : splittedText;

			String[] langPairs = TranslateAction.getLangPair();
			boolean autoDetect = TranslateAction.isAutoDetect();
			List<String> translated = new YandexTranslator().translate(splittedText, langPairs, autoDetect);
			if (translated != null) {
				mHandler.handleResult(editor, translated);
			} else {
				mHandler.handleError(editor);
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
}
