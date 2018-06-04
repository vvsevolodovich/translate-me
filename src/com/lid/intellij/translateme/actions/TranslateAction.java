package com.lid.intellij.translateme.actions;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.codeInsight.hint.HintManagerImpl;
import com.intellij.codeInsight.hint.HintUtil;
import com.intellij.ide.BrowserUtil;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.ui.LightweightHint;
import com.intellij.xml.util.XmlStringUtil;
import com.lid.intellij.translateme.ResultDialog;
import com.lid.intellij.translateme.configuration.ConfigurationState;
import com.lid.intellij.translateme.configuration.PersistingService;

import javax.swing.JComponent;
import javax.swing.event.HyperlinkEvent;
import java.awt.Point;
import java.util.List;

import static javax.swing.event.HyperlinkEvent.EventType.ACTIVATED;

public class TranslateAction extends EditorAction {
    static final NotificationGroup NOTIFICATION_GROUP = new NotificationGroup("TranslateMe Alerts", NotificationDisplayType.STICKY_BALLOON, false, null);

    public TranslateAction() {
        super(new TranslateHandler(new PopupActionHandler()));
    }

    public static String[] getLangPair() {
        ConfigurationState state = PersistingService.getInstance().getState();

        String from = state.getLangFrom();
        String to = state.getLangTo();
        return new String[] { from, to };
    }

    public static boolean isAutoDetect() {
        ConfigurationState state = PersistingService.getInstance().getState();
        return state.isAutoDetect();
    }

    public static boolean isTranslationTooltip() {
        ConfigurationState state = PersistingService.getInstance().getState();
        return state.isTranslationTooltip();
    }

    private static class PopupActionHandler implements ActionHandler {
        @Override
        public void handleResult(Editor editor, List<String> translated) {
            Application app = ApplicationManager.getApplication();
            app.invokeLater(() -> {
                if (isTranslationTooltip()) {
                    StringBuilder sb = new StringBuilder();
                    int iMax = translated.size();
                    String sep = "";
                    for (int i = 0; i < iMax; i++) {
                        sb.append(sep).append(translated.get(i));
                        sep = "\n";
                    }
                    sb.append("<p><a href='http://translate.yandex.com/'>Powered by Yandex.Translator</a>");
                    showTooltip(editor, sb.toString());
                } else {
                    ResultDialog resultDialog = new ResultDialog("Translated", translated);
                    resultDialog.setVisible(true);
                }
            });
        }

        @Override
        public void handleError(Editor editor) {
            Application app = ApplicationManager.getApplication();
            app.invokeLater(() -> {
                showErrorBalloon(editor, "Failed to translate");
            });
        }

        private void showErrorBalloon(Editor editor, String message) {
            NOTIFICATION_GROUP.createNotification("TranslateMe Error", XmlStringUtil.wrapInHtml(message), NotificationType.ERROR, (notification, event) -> {
            }).notify(null);
        }

        private void showTooltip(Editor editor, String message) {
            //String text = "<table><tr><td>&nbsp;</td><td>$message</td><td>&nbsp;</td></tr></table>"
            HintManagerImpl hintManager = (HintManagerImpl) HintManager.getInstance();
            int flags = HintManager.HIDE_BY_CARET_MOVE | HintManager.UPDATE_BY_SCROLLING | HintManager.HIDE_BY_ESCAPE;
            int timeout = 60000; // default 1min?
            String html = HintUtil.prepareHintText(message, HintUtil.getInformationHint());

            JComponent label = HintUtil.createInformationLabel(html, e -> {
                if (e.getEventType() == ACTIVATED) {
                    BrowserUtil.browse("http://translate.yandex.com/");
                }
            }, null, null);

            LightweightHint hint = new LightweightHint(label);

            VisualPosition start = editor.offsetToVisualPosition(editor.getSelectionModel().getSelectionStart());
            VisualPosition end = editor.offsetToVisualPosition(editor.getSelectionModel().getSelectionEnd());

            CharSequence chars = editor.getDocument().getCharsSequence();
            int i = editor.getSelectionModel().getSelectionStart();
            int iMax = editor.getSelectionModel().getSelectionEnd();
            int maxColumn = Math.max(start.column,end.column);
            for (; i < iMax; ++i) {
                if (i > 0 && chars.charAt(i) == '\n') {
                    VisualPosition vis = editor.offsetToVisualPosition(i - 1);
                    if (maxColumn < vis.column) {
                        maxColumn = vis.column;
                    }
                }
            }

            VisualPosition pos = new VisualPosition(start.line, (start.column + maxColumn) / 2, false);

            Point p = hintManager.getHintPosition(hint, editor, pos, HintManager.ABOVE);
            hintManager.showEditorHint(hint, editor, p, flags, timeout, false);
        }
    }
}
