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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.JPanel;
import org.openstreetmap.josm.Main;


/**
 * Defines an abstract {@code JPanel} template for displaying basic information in (information name, value) format of
 * an object of type T.
 *
 * @author Beata
 * @version $Revision$
 */
public abstract class BasicPanel<T> extends JPanel {

    private static final long serialVersionUID = -1712483793218733712L;

    /** the minimum size of the panel */
    public static final Dimension PANEL_MIN = new Dimension(0, 10);

    private final Font fontBold;
    private final Font fontPlain;

    /* font metrics */
    private final FontMetrics fontMetricsPlain;
    private final FontMetrics fontMetricsBold;

    /* constants used for computing GUI component dimensions */
    protected static final int LHEIGHT = 22;
    protected static final int SPACE_Y = 3;
    protected static final int RECT_X = 0;
    protected static final int RECT_Y = 0;
    private int pnlY = 0;
    private int pnlWidth = 0;


    /**
     * Builds a new {@code InfoPanel} with the given argument.
     */
    public BasicPanel() {
        setBackground(Color.white);
        fontBold = getFont().deriveFont(Font.BOLD, GuiBuilder.FONT_SIZE);
        fontPlain = getFont().deriveFont(Font.PLAIN, GuiBuilder.FONT_SIZE);
        fontMetricsPlain = Main.map.mapView.getGraphics().getFontMetrics(fontPlain);
        fontMetricsBold = Main.map.mapView.getGraphics().getFontMetrics(fontBold);
    }


    /**
     * Creates the components of the panel with the information of the given object.
     *
     * @param obj an object of type T
     */
    protected abstract void createComponents(T obj);

    /**
     * Returns the maximum width for the given strings.
     *
     * @param fm the {@code FontMetrics} used for displaying the given strings
     * @param strings s list of {@code String}s
     * @return the maximum width
     */
    protected int getMaxWidth(final FontMetrics fm, final String... strings) {
        int width = 0;
        for (int i = 0; i < strings.length; i++) {
            width = Math.max(width, fm.stringWidth(strings[i]) + SPACE_Y);
        }
        return width;
    }

    /**
     * Updates the panel with the information of the given object.
     *
     * @param obj an object of type T
     */
    public void updateData(final T obj) {
        removeAll();
        if (obj != null) {
            pnlY = 0;
            pnlWidth = 0;
            setLayout(null);
            createComponents(obj);
        } else {
            setPreferredSize(PANEL_MIN);
        }
    }

    protected void incrementPnlY() {
        pnlY = pnlY + LHEIGHT;
    }

    protected void setPnlY(final int value) {
        pnlY = value;
    }
    protected void setPnlWidth(final int value) {
        pnlWidth = Math.max(pnlWidth, value);
    }

    protected int getPnlY() {
        return pnlY;
    }

    protected int getPnlWidth() {
        return pnlWidth;
    }

    protected Font getFontBold() {
        return fontBold;
    }

    protected Font getFontPlain() {
        return fontPlain;
    }

    protected FontMetrics getFontMetricsPlain() {
        return fontMetricsPlain;
    }

    protected FontMetrics getFontMetricsBold() {
        return fontMetricsBold;
    }
}