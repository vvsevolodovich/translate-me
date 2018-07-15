package com.lid.intellij.translateme.actions;

import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.xml.util.XmlStringUtil;
import com.lid.intellij.translateme.ResultDialog;
import com.lid.intellij.translateme.configuration.ConfigurationState;
import com.lid.intellij.translateme.configuration.PersistingService;

import java.util.List;

public class TranslateAction extends EditorAction {

    private static final NotificationGroup NOTIFICATION_GROUP
            = new NotificationGroup("TranslateMe Alerts", NotificationDisplayType.STICKY_BALLOON, false, null);

    public TranslateAction() {
        super(new TranslateHandler(new PopupActionHandler()));
    }

    public static String[] getLangPair() {
        ConfigurationState state = PersistingService.getInstance().getState();

        String from = state.getLangFrom();
        String to = state.getLangTo();
        return new String[]{from, to};
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
                    String sep = "";
                    for (String word : translated) {
                        sb.append(sep).append(word);
                        sep = "\n";
                    }
                    sb.append("<br><p><a href='https://translate.yandex.com/'>Powered by Yandex.Translator</a>");
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
                showErrorBalloon("Failed to translate");
            });
        }

        private void showErrorBalloon(String message) {
            NOTIFICATION_GROUP.createNotification("TranslateMe Error", XmlStringUtil.wrapInHtml(message), NotificationType.ERROR, (notification, event) -> {
            }).notify(null);
        }

        private void showTooltip(Editor editor, String message) {
            Tooltip.INSTANCE.showTooltip(message, editor);
        }
    }
}
