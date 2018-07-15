package com.lid.intellij.translateme.actions

import com.intellij.codeInsight.hint.HintManager
import com.intellij.codeInsight.hint.HintManagerImpl
import com.intellij.codeInsight.hint.HintUtil
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.VisualPosition
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.openapi.editor.impl.view.FontLayoutService
import com.intellij.ui.LightweightHint

import com.intellij.codeInsight.hint.HintManager.HIDE_BY_CARET_MOVE
import com.intellij.codeInsight.hint.HintManager.HIDE_BY_ESCAPE
import com.intellij.codeInsight.hint.HintManager.UPDATE_BY_SCROLLING
import javax.swing.event.HyperlinkEvent.EventType.ACTIVATED

internal object Tooltip {

    private const val DEFAULT_TIMEOUT = 60 * 1000
    private const val DEFAULT_FLAGS = HIDE_BY_CARET_MOVE or UPDATE_BY_SCROLLING or HIDE_BY_ESCAPE

    fun showTooltip(message: String, editor: Editor) {
        val hintManager = HintManager.getInstance() as HintManagerImpl

        val hint = createHint(message)

        val start = editor.offsetToVisualPosition(editor.selectionModel.selectionStart)
        val end = editor.offsetToVisualPosition(editor.selectionModel.selectionEnd)

        val maxColumn = calculateMaxColumn(editor, start, end)

        val pos = calculatePosition(editor, start, maxColumn)

        val pointAt = HintManagerImpl.getHintPosition(hint, editor, pos, HintManager.ABOVE)
        hintManager.showEditorHint(hint, editor, pointAt, DEFAULT_FLAGS, DEFAULT_TIMEOUT, false)
    }

    private fun calculateMaxColumn(editor: Editor, start: VisualPosition, end: VisualPosition): Int {
        val chars = editor.document.charsSequence
        var i = editor.selectionModel.selectionStart
        val iMax = editor.selectionModel.selectionEnd
        var maxColumn = Math.max(start.column, end.column)
        while (i < iMax) {
            if (i > 0 && chars[i] == '\n') {
                val vis = editor.offsetToVisualPosition(i - 1)
                if (maxColumn < vis.column) {
                    maxColumn = vis.column
                }
            }
            ++i
        }
        return maxColumn
    }

    private fun calculatePosition(editor: Editor, start: VisualPosition, maxColumn: Int): VisualPosition {
        val fm = editor.contentComponent.getFontMetrics(editor.colorsScheme.getFont(EditorFontType.PLAIN))
        val plainSpaceWidth = FontLayoutService.getInstance().charWidth(fm, ' ')
        var width = maxColumn

        if (plainSpaceWidth > 0) {
            width = getVisibleAreaWidth(editor) / plainSpaceWidth
        }
        return VisualPosition(start.line, (start.column + Math.min(maxColumn, width)) / 2, false)
    }

    private fun createHint(message: String): LightweightHint {
        //String text = "<table><tr><td>&nbsp;</td><td>$message</td><td>&nbsp;</td></tr></table>"
        val html = HintUtil.prepareHintText(message, HintUtil.getInformationHint())

        val label = HintUtil.createInformationLabel(html, { e ->
            if (e.eventType == ACTIVATED) {
                BrowserUtil.browse("https://translate.yandex.com/")
            }
        }, null, null)

        return LightweightHint(label)
    }

    private fun getVisibleAreaWidth(editor: Editor): Int {
        val insets = editor.contentComponent.insets
        return Math.max(0, editor.scrollingModel.visibleArea.width - insets.left - insets.right)
    }
}
