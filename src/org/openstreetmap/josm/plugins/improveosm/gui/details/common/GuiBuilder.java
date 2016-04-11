/*
 *  Copyright 2015 Telenav, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;


/**
 * Helper object, used for creating specific GUI elements.
 *
 * @author Beata
 * @version $Revision$
 */
public final class GuiBuilder {

    public static final float FONT_SIZE = 12F;
    private static final int SCROLL_BAR_UNIT = 100;

    private GuiBuilder() {}


    /**
     * Builds an empty panel.
     *
     * @return a {@code JPanel} object
     */
    public static JPanel buildEmptyPanel() {
        final JPanel pnl = new JPanel();
        pnl.setBackground(Color.white);
        return pnl;
    }


    /**
     * Builds a new bordered layout panel that has a center and a south component.
     *
     * @param center a {@code Component} represents the center component
     * @param south a {@code Component} represents the south component
     * @return a {@code JPanel} object
     */
    public static JPanel buildBorderLayoutPanel(final Component center, final Component south) {
        final JPanel pnl = new JPanel(new BorderLayout());
        pnl.add(center, BorderLayout.CENTER);
        pnl.add(south, BorderLayout.SOUTH);
        return pnl;
    }

    /**
     * Builds a new flow layout panel containing the given components.
     *
     * @param alignment defines the panel alignment
     * @param components the {@code Component}s to be added
     * @return a {@code JPanel} object
     */
    public static JPanel buildFlowLayoutPanel(final int alignment, final Component... components) {
        final JPanel pnl = new JPanel(new FlowLayout(alignment));
        for (final Component component : components) {
            pnl.add(component);
        }
        return pnl;
    }

    /**
     * Builds a new {@code JScrollPane} with the given arguments.
     *
     * @param component the component to added into the scroll pane
     * @param backgroundColor the background color of the scroll pane
     * @param borderVisible if true then the border will be visible
     * @return a {@code JScrollPane} object
     */
    public static JScrollPane buildScrollPane(final Component component, final Color backgroundColor,
            final boolean borderVisible) {
        final JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBackground(backgroundColor);
        scrollPane.setAutoscrolls(true);
        if (borderVisible) {
            scrollPane.setBorder(BorderFactory.createLineBorder(Color.gray));
        }
        scrollPane.getHorizontalScrollBar().setUnitIncrement(SCROLL_BAR_UNIT);
        scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_BAR_UNIT);
        return scrollPane;
    }

    /**
     * Builds a {@code JScrollPane} object with the given properties.
     *
     * @param name the name of the scroll pane
     * @param component the component to added into the scroll pane
     * @param backgroundColor the background color of the scroll pane
     * @param prefSize the preferred size of the component
     * @return a {@code JScrollPane} object
     */
    public static JScrollPane buildScrollPane(final String name, final Component component, final Color backgroundColor,
            final Dimension prefSize) {
        final JScrollPane scrollPane = buildScrollPane(component, backgroundColor, false);
        if (name != null) {
            scrollPane.setName(name);
        }
        scrollPane.setMinimumSize(prefSize);
        scrollPane.setPreferredSize(prefSize);

        return scrollPane;
    }

    /**
     * Builds a {@code JTabbedPane} holding the given components.
     *
     * @param components the {@code Component}s to be added
     * @return a {@code JTabbedPane} object
     */
    public static JTabbedPane buildTabbedPane(final Component... components) {
        final JTabbedPane tabbedPane = new JTabbedPane();
        for (final Component component : components) {
            tabbedPane.add(component);
        }
        return tabbedPane;
    }

    /**
     * Builds a {@code JButton} object with the given properties.
     *
     * @param action the {@code AbstractAction} to be executed when the button is clicked
     * @param icon the {@code Icon} to be displayed on the button
     * @param tooltip the tooltip to display on mouse hover
     * @param enabled if true the button is enabled
     * @return a {@code JButton}
     */
    public static JButton buildButton(final AbstractAction action, final Icon icon, final String tooltip,
            final boolean enabled) {
        JButton btn;
        if (action == null) {
            btn = new JButton();
        } else {
            btn = new JButton(action);
        }
        btn.setIcon(icon);
        btn.setToolTipText(tooltip);
        btn.setFocusable(false);
        btn.setEnabled(enabled);
        return btn;
    }

    /**
     * Builds a {@code JRadioButton} with the given arguments.
     *
     * @param text the text to be displayed
     * @param actionCommand the actionCommand to be displayed
     * @param backgroundColor the background color
     * @return a {@code JRadioButton} object
     */
    public static JRadioButton buildRadioButton(final String text, final String actionCommand,
            final Color backgroundColor) {
        final JRadioButton radioButton = new JRadioButton(text);
        radioButton.setActionCommand(actionCommand);
        radioButton.setBackground(backgroundColor);
        radioButton.setFont(radioButton.getFont().deriveFont(Font.PLAIN));
        radioButton.setFocusable(false);
        return radioButton;
    }

    /**
     * Builds a button group with the given arguments.
     *
     * @param buttons the {@code JRadioButton}s to be added
     * @return a {@code ButtonGroup} object
     */
    public static ButtonGroup buildButtonGroup(final JRadioButton... buttons) {
        final ButtonGroup btnGroup = new ButtonGroup();
        for (final JRadioButton button : buttons) {
            btnGroup.add(button);
        }
        return btnGroup;
    }

    /**
     * Builds a {@code JButton} with the given arguments.
     *
     * @param action the action to be executed when the button is clicked
     * @param text the text to be displayed
     * @return a {@code JButton} object
     */
    public static JButton buildButton(final AbstractAction action, final String text) {
        final JButton btn = new JButton(action);
        btn.setFont(btn.getFont().deriveFont(Font.BOLD));
        btn.setText(text);
        btn.setFocusable(false);
        return btn;
    }

    /**
     * Builds a new {@code JCheckBox} with the given arguments.
     *
     * @param text the text to be displayed
     * @param actionCommand the actionCommand to be displayed
     * @param backgroundColor the background color
     * @return a {@code JCheckBox} object
     */
    public static JCheckBox buildCheckBox(final String text, final String actionCommand, final Color backgroundColor) {
        final JCheckBox cbbox = new JCheckBox(text);
        cbbox.setActionCommand(actionCommand);
        if (backgroundColor != null) {
            cbbox.setBackground(backgroundColor);
        }
        cbbox.setFont(cbbox.getFont().deriveFont(Font.PLAIN));
        cbbox.setFocusPainted(false);
        return cbbox;
    }

    public static JCheckBox buildCheckBox(final ActionListener actionListener, final String text,
            final Color backgroundColor) {
        final JCheckBox cbbox = buildCheckBox(text, null, backgroundColor);
        cbbox.addActionListener(actionListener);
        return cbbox;
    }

    /**
     * Builds a new {@code JLabel} with the given arguments.
     *
     * @param text the text which will be shown on the label
     * @param textColor the color to be used for the displayed text
     * @param visible specifies if the label is visible or not
     * @return a {@code JLabel} object
     */
    public static JLabel buildLabel(final String text, final Color textColor, final boolean visible) {
        final JLabel lbl = buildLabel(text, null, null);
        lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
        lbl.setForeground(textColor);
        lbl.setVisible(visible);
        return lbl;
    }

    /**
     * Builds a {@code JLabel} with the given properties.
     *
     * @param text the text which will be shown on the label
     * @param font the font of the label's text
     * @param bounds the dimension and location of the label
     * @return a new {@code JLabel} object
     */
    public static JLabel buildLabel(final String text, final Font font, final Rectangle bounds) {
        final JLabel lbl = new JLabel(text);
        if (font != null) {
            lbl.setFont(font);
        }
        lbl.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        lbl.setHorizontalTextPosition(SwingConstants.LEFT);
        lbl.setVerticalTextPosition(SwingConstants.TOP);
        if (bounds != null) {
            lbl.setBounds(bounds);
        }
        return lbl;
    }

    /**
     * Builds a new {@code JTabbedPane} with the given arguments.
     *
     * @param txt the text to be displayed on the text pane
     * @param background the background color
     * @return a {@code JTextPane} object
     */
    public static JTextPane buildTextPane(final String txt, final Color background) {
        final JTextPane txtPane = new JTextPane();
        if (background != null) {
            txtPane.setBackground(background);
        }
        txtPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        txtPane.setCaretPosition(0);
        txtPane.setEditable(false);
        txtPane.setContentType("text/html");
        txtPane.setText(txt);
        return txtPane;
    }

    /**
     * Builds a new {@code JEditorPane} with the given arguments.
     *
     * @param txt the text to be displayed on the text pane
     * @param listener the {code HyperlinkListener} the corresponding listener
     * @return a {@code JEditorPane} object
     */
    public static JEditorPane buildEditorPane(final String txt, final HyperlinkListener listener) {
        final JEditorPane txtEditor = new JEditorPane("text/html", "");
        txtEditor.setEditorKit(new HTMLEditorKit());
        txtEditor.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        txtEditor.setEditable(false);
        txtEditor.setText(txt);
        txtEditor.addHyperlinkListener(listener);
        return txtEditor;
    }

    /**
     * Builds a new {@code JTextArea} with the given arguments.
     *
     * @param text the text to be displayed
     * @param backgroundColor the background color of the scroll pane
     * @param editable specifies if the text component can be edited or not
     * @return a {@code JTextArea} object
     */
    public static JTextArea buildTextArea(final String text, final Color backgroundColor, final boolean editable) {
        final JTextArea txtArea = new JTextArea(text);
        txtArea.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        txtArea.setBackground(backgroundColor);
        txtArea.setFont(txtArea.getFont().deriveFont(FONT_SIZE));
        txtArea.setLineWrap(true);
        txtArea.setWrapStyleWord(true);
        txtArea.setFont(txtArea.getFont().deriveFont(Font.PLAIN));
        txtArea.setEditable(editable);
        return txtArea;
    }

    /**
     * Builds a new {@code JTextField} object with the given arguments. The copy menu & functionality will be enabled
     * for this field.
     *
     * @param txt the text to be displayed on the text field
     * @param bgColor the background color
     * @return a {@code JTextField} object
     */
    public static JTextField buildTextField(final String txt, final Color bgColor) {
        final JTextField txtField = new JTextField(txt);
        txtField.setFont(txtField.getFont().deriveFont(Font.PLAIN));
        txtField.setBackground(bgColor);
        txtField.setDragEnabled(true);
        txtField.setEditable(true);
        return txtField;
    }

    /**
     * Builds a new {@code JMenuItem} object with the given arguments.
     *
     * @param action the {@code AbstractAction} to be executed when the menu item is selected
     * @param title the menu item's title
     * @return a {@code JMenuItem} object
     */
    public static JMenuItem buildMenuItem(final AbstractAction action, final String title) {
        final JMenuItem menuItem = new JMenuItem(action);
        menuItem.setText(title);
        return menuItem;
    }
}