package com.lid.intellij.translateme.actions;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import com.lid.intellij.translateme.ResultDialog;
import com.lid.intellij.translateme.configuration.ConfigurationState;

import java.awt.*;
import java.util.List;

public class TranslateAction extends EditorAction {

	public TranslateAction() {
		super(new TranslateHandler(new PopupActionHandler()));
	}

	public static String[] getLangPair(Project project) {

		if (project != null) {
			ConfigurationState state = ConfigurationState.getInstance();

			String from = state.getFrom();
			String to = state.getTo();
			return new String[]{from, to};
		}

		return new String[]{"en", "ru"};
	}

	public static boolean isAutoDetect(Project project) {
		if (project != null) {
			ConfigurationState state = ConfigurationState.getInstance();
			return state.isAutoDetect();
		}

		return false;
	}

	private static class PopupActionHandler implements ActionHandler {
		@Override
		public void handleResult(Editor editor, List<String> translated) {
			Application app = ApplicationManager.getApplication();
			app.invokeLater(() -> {
                ResultDialog resultDialog = new ResultDialog("Translated", translated);
                resultDialog.setVisible(true);
            });
		}

		@Override
		public void handleError(Editor editor) {
			Application app = ApplicationManager.getApplication();
			app.invokeLater(() -> {
				showErrorBallon(editor, "Failed to translate");
			});
		}

		private void showErrorBallon(Editor editor, String message) {
			BalloonBuilder builder =
					JBPopupFactory.getInstance().createHtmlTextBalloonBuilder("hello", MessageType.ERROR, null);
			Balloon balloon = builder.createBalloon();
			balloon.setTitle(message);
			CaretModel caretModel = editor.getCaretModel();
			Point point = editor.visualPositionToXY(caretModel.getVisualPosition());
			RelativePoint where = new RelativePoint(point);
			balloon.show(where, Balloon.Position.below);
		}
	}
}
