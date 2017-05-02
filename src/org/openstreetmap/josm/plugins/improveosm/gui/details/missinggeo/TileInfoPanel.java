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
package org.openstreetmap.josm.plugins.improveosm.gui.details.missinggeo;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.entity.TileType;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.MissingGeometryGuiConfig;
import com.telenav.josm.common.formatter.EntityFormatter;
import com.telenav.josm.common.gui.BasicInfoPanel;
import com.telenav.josm.common.gui.builder.LabelBuilder;


/**
 * Displays the information of a selected {@code Tile}
 *
 * @author Beata
 * @version $Revision$
 */
public class TileInfoPanel extends BasicInfoPanel<Tile> {

    private static final long serialVersionUID = 5842933383198993565L;


    public TileInfoPanel() {
        super();
    }


    @Override
    public void createComponents(final Tile tile) {
        final MissingGeometryGuiConfig mgGuiCnf = MissingGeometryGuiConfig.getInstance();
        final FontMetrics fm = getFontMetrics(getFont().deriveFont(Font.BOLD));
        final int widthLbl = getMaxWidth(fm, mgGuiCnf.getLblType(), mgGuiCnf.getLblPointCount(),
                mgGuiCnf.getLblTripCount(), GuiConfig.getInstance().getLblStatus(), mgGuiCnf.getLblTimestamp());

        addType(tile.getType(), widthLbl);
        addStatus(tile.getStatus(), widthLbl);
        addTimestamp(tile.getTimestamp(), widthLbl);
        addNumberOfTrips(tile.getNumberOfTrips(), widthLbl);
        addNumberOfPoints(tile.getPoints(), widthLbl);
        final int pnlHeight = getPnlY() + SPACE_Y;
        setPreferredSize(new Dimension(getPnlWidth() + SPACE_Y, pnlHeight));
    }

    private void addType(final TileType type, final int widthLbl) {
        if (type != null) {
            add(LabelBuilder.build(MissingGeometryGuiConfig.getInstance().getLblType(), Font.BOLD,
                    ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            final int widthVal = getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(type.toString());
            add(LabelBuilder.build(type.toString(), Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                    SwingConstants.TOP, new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    private void addStatus(final Status status, final int widthLbl) {
        if (status != null) {
            add(LabelBuilder.build(GuiConfig.getInstance().getLblStatus(), Font.BOLD,
                    ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            final int widthVal = getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(status.name());
            add(LabelBuilder.build(status.name(), Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                    SwingConstants.TOP, new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    private void addTimestamp(final Long timestamp, final int widthLbl) {
        if (timestamp != null) {
            add(LabelBuilder.build(MissingGeometryGuiConfig.getInstance().getLblTimestamp(), Font.BOLD,
                    ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            final String timestampStr = EntityFormatter.formatTimestamp(timestamp);
            final int widthVal = getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(timestampStr);
            add(LabelBuilder.build(timestampStr, Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                    SwingConstants.TOP, new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    private void addNumberOfPoints(final List<LatLon> points, final int widthLbl) {
        if (points != null) {
            add(LabelBuilder.build(MissingGeometryGuiConfig.getInstance().getLblPointCount(), Font.BOLD,
                    ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            final String numberOfPoints =
                    points.size() > 1 ? Integer.toString(points.size()) : GuiConfig.getInstance().getLblNotAvailable();
            final int widthVal = getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(numberOfPoints);
            add(LabelBuilder.build(numberOfPoints, Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                    SwingConstants.TOP, new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    private void addNumberOfTrips(final Integer numberOfTrips, final int widthLbl) {
        if (numberOfTrips != null) {
            add(LabelBuilder.build(MissingGeometryGuiConfig.getInstance().getLblTripCount(), Font.BOLD,
                    ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            final String numberOfTripsTxt =
                    numberOfTrips > -1 ? numberOfTrips.toString() : GuiConfig.getInstance().getLblNotAvailable();
            final int widthVal = getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(numberOfTripsTxt);
            add(LabelBuilder.build(numberOfTripsTxt, Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT,
                    SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }
}