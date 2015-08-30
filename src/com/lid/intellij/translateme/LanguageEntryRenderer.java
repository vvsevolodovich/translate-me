package com.lid.intellij.translateme;

import com.intellij.ui.ListCellRendererWrapper;
import org.jetbrains.jps.api.CmdlineRemoteProto.Message.KeyValuePair;

import javax.swing.*;
import java.awt.*;

public class LanguageEntryRenderer extends ListCellRendererWrapper {


	/**
	 * Creates new list renderer
	 */
	public LanguageEntryRenderer() {
	}



	/**
	 * Return a component that has been configured to display the
	 * specified value. Contains main logic for the renderer,
	 */
	/*public Component getListCellRendererComponent(JList listbox, Object value,
	                                              int index, boolean isSelected, boolean cellHasFocus) {
		KeyValuePair pair = (KeyValuePair) value;

		if (pair != null) {
			this.setText(pair.getValue());
		}

		if (isSelected) {
			this.setBackground(UIManager.getColor("ComboBox.selectionBackground"));
			this.setForeground(UIManager.getColor("ComboBox.selectionForeground"));
		} else {
			this.setBackground(UIManager.getColor("ComboBox.background"));
			this.setForeground(UIManager.getColor("ComboBox.foreground"));
		}

		return this;
	}*/

	@Override
	public void customize(JList jList, Object o, int i, boolean b, boolean b1) {

	}

}
