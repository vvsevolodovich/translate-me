package com.lid.intellij.translateme;

import com.intellij.ide.BrowserUtil;
import com.intellij.ui.components.labels.LinkLabel;
import com.intellij.ui.components.labels.LinkListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class ResultDialog extends JDialog implements ActionListener {

    public ResultDialog(String message, java.util.List<String> detail) {
        super(JOptionPane.getRootFrame(), message, true);

        final JPanel content = new JPanel();

	    Box verticalBox = Box.createVerticalBox();

	    for (int i  = 0; i < detail.size(); i++) {
		    verticalBox.add(new JLabel(i + 1 + ". " + detail.get(0)));
		    verticalBox.add(Box.createVerticalGlue());
	    }

	    final LinkLabel showMoreLink = createLinkLabel();
	    verticalBox.add(showMoreLink);

	    content.add(verticalBox);

        getContentPane().add(content, BorderLayout.NORTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(getOwner());

	    addEscapeListener(this);
    }

    @NotNull
    private LinkLabel createLinkLabel() {

        final LinkLabel showMoreLink = new LinkLabel("Powered by Yandex.Translator", null);
        LinkListener showMoreListener = new LinkListener() {

            public void linkSelected(LinkLabel aSource, Object aLinkData) {
	            BrowserUtil.browse("http://translate.yandex.com/");
            }
        };
        showMoreLink.setListener(showMoreListener, null);
        return showMoreLink;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        setVisible(false);
        dispose();
    }

	public static void addEscapeListener(final JDialog dialog) {
		ActionListener escListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		};

		dialog.getRootPane().registerKeyboardAction(escListener,
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

	}
}