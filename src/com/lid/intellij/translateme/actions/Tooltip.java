package com.lid.intellij.translateme.actions;

import com.intellij.codeInsight.hint.HintManager;
import com.intellij.codeInsight.hint.HintManagerImpl;
import com.intellij.codeInsight.hint.HintUtil;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.openapi.editor.impl.view.FontLayoutService;
import com.intellij.ui.LightweightHint;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static com.intellij.codeInsight.hint.HintManager.HIDE_BY_CARET_MOVE;
import static com.intellij.codeInsight.hint.HintManager.HIDE_BY_ESCAPE;
import static com.intellij.codeInsight.hint.HintManager.UPDATE_BY_SCROLLING;
import static javax.swing.event.HyperlinkEvent.EventType.ACTIVATED;

class Tooltip {

    private static final int DEFAULT_TIMEOUT = 60 * 1000;
    private static final int DEFAULT_FLAGS =  HIDE_BY_CARET_MOVE | UPDATE_BY_SCROLLING | HIDE_BY_ESCAPE;

    static void showTooltip(String message, Editor editor) {
        final HintManagerImpl hintManager = (HintManagerImpl) HintManager.getInstance();

        final LightweightHint hint = createHint(message);

        VisualPosition start = editor.offsetToVisualPosition(editor.getSelectionModel().getSelectionStart());
        VisualPosition end = editor.offsetToVisualPosition(editor.getSelectionModel().getSelectionEnd());

        int maxColumn = calculateMaxColumn(editor, start, end);

        VisualPosition pos = calculatePosition(editor, start, maxColumn);

        Point pointAt = HintManagerImpl.getHintPosition(hint, editor, pos, HintManager.ABOVE);
        hintManager.showEditorHint(hint, editor, pointAt, DEFAULT_FLAGS, DEFAULT_TIMEOUT, false);
    }

    private static int calculateMaxColumn(Editor editor, VisualPosition start, VisualPosition end) {
        CharSequence chars = editor.getDocument().getCharsSequence();
        int i = editor.getSelectionModel().getSelectionStart();
        int iMax = editor.getSelectionModel().getSelectionEnd();
        int maxColumn = Math.max(start.column, end.column);
        for (; i < iMax; ++i) {
            if (i > 0 && chars.charAt(i) == '\n') {
                VisualPosition vis = editor.offsetToVisualPosition(i - 1);
                if (maxColumn < vis.column) {
                    maxColumn = vis.column;
                }
            }
        }
        return maxColumn;
    }

    @NotNull
    private static VisualPosition calculatePosition(Editor editor, VisualPosition start, int maxColumn) {
        FontMetrics fm = editor.getContentComponent().getFontMetrics(editor.getColorsScheme().getFont(EditorFontType.PLAIN));
        int plainSpaceWidth = FontLayoutService.getInstance().charWidth(fm, ' ');
        int width = maxColumn;

        if (plainSpaceWidth > 0) {
            width = getVisibleAreaWidth(editor) / plainSpaceWidth;
        }
        return new VisualPosition(start.line, (start.column + Math.min(maxColumn, width)) / 2, false);
    }

    @NotNull
    private static LightweightHint createHint(String message) {
        //String text = "<table><tr><td>&nbsp;</td><td>$message</td><td>&nbsp;</td></tr></table>"
        String html = HintUtil.prepareHintText(message, HintUtil.getInformationHint());

        JComponent label = HintUtil.createInformationLabel(html, e -> {
            if (e.getEventType() == ACTIVATED) {
                BrowserUtil.browse("https://translate.yandex.com/");
            }
        }, null, null);

        return new LightweightHint(label);
    }

    private static int getVisibleAreaWidth(Editor editor) {
        Insets insets = editor.getContentComponent().getInsets();
        int width = Math.max(0, editor.getScrollingModel().getVisibleArea().width - insets.left - insets.right);
        return width;
    }
}
