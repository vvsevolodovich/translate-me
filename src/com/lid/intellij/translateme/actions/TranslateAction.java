package com.lid.intellij.translateme.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import com.lid.intellij.translateme.Configuration;
import com.lid.intellij.translateme.ResultDialog;

import java.awt.*;
import java.util.List;

public class TranslateAction extends EditorAction {

	public TranslateAction() {
		super(new TranslateHandler(new PopupActionHandler()));
	}

	protected final Editor getEditor(AnActionEvent event) {
		Project project = event.getData(PlatformDataKeys.PROJECT);
		return DataKeys.EDITOR.getData(event.getDataContext());
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

	private static class PopupActionHandler implements ActionHandler {
		@Override
		public void handleResult(Editor editor, List<String> translated) {
			ResultDialog resultDialog = new ResultDialog("Translated", translated);
			resultDialog.setVisible(true);
		}

		@Override
		public void handleError(Editor editor) {
			showErrorBallon(editor, "Failed to translate");
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
