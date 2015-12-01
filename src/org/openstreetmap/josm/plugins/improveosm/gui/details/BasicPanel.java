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
package org.openstreetmap.josm.plugins.improveosm.gui.details;

import static org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder.BOLD_12;
import static org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder.PLAIN_12;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import javax.swing.JPanel;
import org.openstreetmap.josm.Main;

/**
 *
 * @author Beata
 * @version $Revision$
 */
public abstract class BasicPanel<T> extends JPanel {

    private static final long serialVersionUID = -1712483793218733712L;

    /** the minimum size of the panel */
    private static final Dimension PANEL_MIN = new Dimension(0, 50);

    /* font metrics */
    protected final FontMetrics FM_PLAIN_12 = Main.map.mapView.getGraphics().getFontMetrics(PLAIN_12);
    protected final FontMetrics FM_BOLD_12 = Main.map.mapView.getGraphics().getFontMetrics(BOLD_12);

    /* constants used for computing GUI component dimensions */
    protected static final int LHEIGHT = 22;
    protected static final int SPACE_Y = 3;
    protected static final int RECT_X = 0;
    protected static final int RECT_Y = 0;

    /**
     * Builds a new {@code InfoPanel} with the given argument.
     */
    public BasicPanel() {
        setBackground(Color.white);
        updateData(null);
    }

    /**
     * Creates the components of the panel with the information of the given object.
     *
     * @param obj an object of type T
     */
    public abstract void createComponents(T obj);

    /**
     * Returns the maximum width for the given strings.
     *
     * @param fm the {@code FontMetrics} used for displaying the given strings
     * @param strings s list of {@code String}s
     * @return the maximum width
     */
    public int getMaxWidth(final FontMetrics fm, final String... strings) {
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
    public final void updateData(final T obj) {
        removeAll();
        if (obj != null) {
            setLayout(null);
            createComponents(obj);
        } else {
            setPreferredSize(PANEL_MIN);
        }
    }
}