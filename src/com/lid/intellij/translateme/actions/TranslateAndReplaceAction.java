package com.lid.intellij.translateme.actions;


import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.actionSystem.EditorAction;

import java.util.List;

public class TranslateAndReplaceAction extends EditorAction {

	protected TranslateAndReplaceAction() {
		super(new TranslateHandler(new ActionHandler() {
			@Override
			public void handleResult(Editor editor, List<String> translated) {
				final SelectionModel selectionModel = editor.getSelectionModel();
				final int selectionStart = selectionModel.getSelectionStart();
				final int selectionEnd = selectionModel.getSelectionEnd();

				String oldText = selectionModel.getSelectedText();
				final String newText = translated.isEmpty() ? oldText : translated.get(0);

				/*Document document = editor.getDocument();
				document.deleteString(selectionStart, selectionEnd);
				document.insertString(selectionStart, newText);
				selectionModel.setSelection(selectionStart, selectionStart + newText.length());*/
				EditorModificationUtil.deleteSelectedText(editor);
				EditorModificationUtil.insertStringAtCaret(editor, newText);
				selectionModel.setSelection(selectionStart, selectionStart + newText.length());
			}

			@Override
			public void handleError(Editor editor) {

			}
		}));
	}
}
