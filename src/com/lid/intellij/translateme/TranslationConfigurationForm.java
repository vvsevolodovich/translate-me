package com.lid.intellij.translateme;

import com.google.gson.Gson;
import com.intellij.openapi.ui.ComboBox;
import com.lid.intellij.translateme.yandex.LangsResponse;
import com.lid.intellij.translateme.yandex.YandexClient;
import org.jetbrains.annotations.NotNull;

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
	private final Checkbox autoDetect;

	public TranslationConfigurationForm() {
		rootComponent = new JPanel();
		rootComponent.setPreferredSize(new Dimension(200,200));
		//rootComponent.setBackground(Color.blue);
		rootComponent.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		JPanel languages = createLanguages();
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.EAST;
		c.gridx = 0;
		c.gridy = 0;
		rootComponent.add(languages, c);

		GridBagConstraints c2 = new GridBagConstraints();
		c.anchor = GridBagConstraints.EAST;
		c2.fill = GridBagConstraints.EAST;
		c2.gridx= 0;
		c2.gridy = 1;
		autoDetect = new Checkbox("Auto-detect");

		initCheckbox(autoDetect);
		rootComponent.add(autoDetect, c2);
	}

	@NotNull
	private JPanel createLanguages() {
		JPanel languages = new JPanel();
		//languages.setAlignmentX(Component.LEFT_ALIGNMENT);
		//languages.setAlignmentY(Component.TOP_ALIGNMENT);
		languages.setLayout(new FlowLayout());
		languages.add(label);
		languages.add(comboBoxFrom);
		languages.add(comboBoxTo);

		initComboBox(comboBoxFrom);
		initComboBox(comboBoxTo);
		return languages;
	}

	private void initComboBox(ComboBox comboBox) {
		comboBox.removeAllItems();
		comboBox.setModel(createModel());

		if (comboBox.getModel().getSize() > 0) {
			comboBox.setSelectedIndex(0);
		}
	}

	private void initCheckbox(Checkbox checkbox) {
		//checkbox.setState();
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

	public void save(Configuration data) {
		String selectedItemFrom = (String) comboBoxFrom.getSelectedItem();
		String selectedItemTo = (String) comboBoxTo.getSelectedItem();


		if (selectedItemFrom != null && selectedItemTo != null) {
			data.setLangPair(selectedItemFrom, selectedItemTo);
		}

		data.setAutoDetect(autoDetect.getState());
		return;
	}
	public boolean load(Configuration data) {
		comboBoxFrom.setSelectedItem(data.getFrom());
		comboBoxTo.setSelectedItem(data.getTo());
		autoDetect.setState(data.isAutoDetect());
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
