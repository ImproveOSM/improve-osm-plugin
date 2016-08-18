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

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import com.telenav.josm.common.gui.GuiBuilder;


/**
 * Helper object, used for creating specific GUI elements.
 *
 * @author Beata
 * @version $Revision$
 */
public final class Builder {

    private static final int SCROLL_BAR_UNIT = 100;

    private Builder() {}

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
        final JScrollPane scrollPane = GuiBuilder.buildScrollPane(component, backgroundColor);
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
     * Builds a new {@code JCheckBox} with the given arguments.
     *
     * @param text the text to be displayed
     * @param actionCommand the actionCommand to be displayed
     * @param backgroundColor the background color
     * @return a {@code JCheckBox} object
     */
    public static JCheckBox buildCheckBox(final String text, final String actionCommand, final Color backgroundColor) {
        final JCheckBox cbbox =
                GuiBuilder.buildCheckBox(text, new JCheckBox().getFont().deriveFont(Font.PLAIN), null, false, false);
        if (backgroundColor != null) {
            cbbox.setBackground(backgroundColor);
        }
        cbbox.setActionCommand(actionCommand);
        return cbbox;
    }

    public static JCheckBox buildCheckBox(final ActionListener actionListener, final String text,
            final Color backgroundColor) {
        final JCheckBox cbbox = GuiBuilder.buildCheckBox(text, new JCheckBox().getFont().deriveFont(Font.PLAIN),
                actionListener, false, false);
        if (backgroundColor != null) {
            cbbox.setBackground(backgroundColor);
        }
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
        final JLabel lbl = GuiBuilder.buildLabel(text, font, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                SwingConstants.TOP);
        if (bounds != null) {
            lbl.setBounds(bounds);
        }
        return lbl;
    }

}