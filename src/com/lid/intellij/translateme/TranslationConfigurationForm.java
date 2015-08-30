package com.lid.intellij.translateme;

import com.google.gson.Gson;
import com.intellij.openapi.ui.ComboBox;
import com.lid.intellij.translateme.yandex.LangsResponse;
import com.lid.intellij.translateme.yandex.YandexClient;
import org.jetbrains.jps.api.CmdlineRemoteProto;
import org.jetbrains.jps.api.CmdlineRemoteProto.Message.KeyValuePair;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class TranslationConfigurationForm {
	private JPanel rootComponent;

	private ComboBox comboBoxFrom = new ComboBox();
	private ComboBox comboBoxTo = new ComboBox();
	private JLabel label = new JLabel("Select translation:");

	private LangsResponse mLangsResponse;

	public TranslationConfigurationForm() {
		rootComponent = new JPanel();

		rootComponent.setLayout(new FlowLayout());

		rootComponent.add(label);
		rootComponent.add(comboBoxFrom);
		rootComponent.add(comboBoxTo);

		initComboBox(comboBoxFrom);
		initComboBox(comboBoxTo);
	}

	private void initComboBox(ComboBox comboBox) {
		comboBox.removeAllItems();
		comboBox.setModel(createModel());
		//comboBox.setRenderer(new LanguageEntryRenderer());

		if (comboBox.getModel().getSize() > 0) {
			comboBox.setSelectedIndex(0);
		}
	}

	private ComboBoxModel createModel() {
		if (mLangsResponse == null) {
			List<String> items = new ArrayList<>();
			try {
				String responseText = new YandexClient().getLanguages("ru");
				mLangsResponse = new Gson().fromJson(responseText, LangsResponse.class);
			} catch (Exception e) {
				// TODO : failed to get, log
			}
		}
		List<String> items = new ArrayList<>();
		items.addAll(mLangsResponse.getLangs().keySet());
		return new DefaultComboBoxModel(items.toArray());
	}

	/**
	 * Gets the root component of the form.
	 *
	 * @return root component of the form
	 */
	public JComponent getRootComponent() {
		return rootComponent;
	}

	/**
	 * Setter for property 'data'.
	 *
	 * @param data Value to set for property 'data'.
	 */
	/*public void load(TranslateConfiguration data) {
		String langPair = data.getLangPair();

		ComboBoxModel model = comboBox.getModel();

		boolean ok = false;

		for (int i = 0; i < model.getSize() && !ok; i++) {
			KeyValuePair item = (KeyValuePair) model.getElementAt(i);

			if (item.getKey().equals(langPair)) {
				comboBox.setSelectedItem(item);
				ok = true;
			}
		}
	}*/

	public void save(Configuration data) {
		String selectedItemFrom = (String) comboBoxFrom.getSelectedItem();
		String selectedItemTo = (String) comboBoxTo.getSelectedItem();


		if (selectedItemFrom != null && selectedItemTo != null) {
			data.setLangPair(selectedItemFrom, selectedItemTo);
		}
		return;
	}
	public boolean load(Configuration data) {
		comboBoxFrom.setSelectedItem(data.getFrom());
		comboBoxTo.setSelectedItem(data.getTo());
		return true;
	}

	public boolean isModified(Configuration data) {
		return true;
	}
	/*public boolean isModified(TranslateConfiguration data) {
		KeyValuePair selectedItem = (KeyValuePair) comboBox.getSelectedItem();

		return selectedItem != null ?
				!selectedItem.getKey().equals(data.getLangPair()) :
				data.getLangPair() != null;
	}*/

}
