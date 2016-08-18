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

import java.awt.FontMetrics;
import org.openstreetmap.josm.Main;
import com.telenav.josm.common.gui.BasicInfoPanel;


/**
 * Defines an abstract {@code JPanel} template for displaying basic information in (information name, value) format of
 * an object of type T.
 *
 * @author Beata
 * @version $Revision$
 * @param <T> the type of the object who's details is displayed in the panel
 */
public abstract class BasicPanel<T> extends BasicInfoPanel<T> {

    private static final long serialVersionUID = -1712483793218733712L;

    /* font metrics */
    private final FontMetrics fontMetricsPlain;
    private final FontMetrics fontMetricsBold;

    /* constant used for computing GUI component dimensions */
    protected static final int RECT_Y = 0;


    /**
     * Builds a new {@code InfoPanel} with the given argument.
     */
    public BasicPanel() {
        super();
        fontMetricsPlain = Main.map.mapView.getGraphics().getFontMetrics(getFontPlain());
        fontMetricsBold = Main.map.mapView.getGraphics().getFontMetrics(getFontBold());
    }

    protected FontMetrics getFontMetricsPlain() {
        return fontMetricsPlain;
    }

    protected FontMetrics getFontMetricsBold() {
        return fontMetricsBold;
    }
}