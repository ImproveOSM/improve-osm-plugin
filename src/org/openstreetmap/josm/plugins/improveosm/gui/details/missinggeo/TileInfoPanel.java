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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.entity.TileType;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.BasicPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.Formatter;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.GuiBuilder;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.MissingGeometryGuiConfig;


/**
 * Displays the information of a selected {@code Tile}
 *
 * @author Beata
 * @version $Revision$
 */
public class TileInfoPanel extends BasicPanel<Tile> {

    private static final long serialVersionUID = 5842933383198993565L;


    public TileInfoPanel() {
        super();
    }


    @Override
    public void createComponents(final Tile tile) {
        final MissingGeometryGuiConfig mgGuiCnf = MissingGeometryGuiConfig.getInstance();
        final int widthLbl = getMaxWidth(getFontMetricsBold(), mgGuiCnf.getLblType(),
                mgGuiCnf.getLblPointCount(), mgGuiCnf.getLblTripCount(), GuiConfig.getInstance().getLblStatus(),
                mgGuiCnf.getLblTimestamp());

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
            add(GuiBuilder.buildLabel(MissingGeometryGuiConfig.getInstance().getLblType(), getFontBold(),
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LHEIGHT)));
            final int widthVal = getFontMetricsPlain().stringWidth(type.toString());
            add(GuiBuilder.buildLabel(type.toString(), getFontPlain(),
                    new Rectangle(widthLbl, getPnlY(), widthVal, LHEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    private void addStatus(final Status status, final int widthLbl) {
        if (status != null) {
            add(GuiBuilder.buildLabel(GuiConfig.getInstance().getLblStatus(), getFontBold(),
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LHEIGHT)));
            final int widthVal = getFontMetricsPlain().stringWidth(status.name());
            add(GuiBuilder.buildLabel(status.name(), getFontPlain(),
                    new Rectangle(widthLbl, getPnlY(), widthVal, LHEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    private void addTimestamp(final Long timestamp, final int widthLbl) {
        if (timestamp != null) {
            add(GuiBuilder.buildLabel(MissingGeometryGuiConfig.getInstance().getLblTimestamp(), getFontBold(),
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LHEIGHT)));
            final String timestampStr = Formatter.formatTimestamp(timestamp);
            final int widthVal = getFontMetricsPlain().stringWidth(timestampStr);
            add(GuiBuilder.buildLabel(timestampStr, getFontPlain(),
                    new Rectangle(widthLbl, getPnlY(), widthVal, LHEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    private void addNumberOfPoints(final List<LatLon> points, final int widthLbl) {
        if (points != null) {
            add(GuiBuilder.buildLabel(MissingGeometryGuiConfig.getInstance().getLblPointCount(), getFontBold(),
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LHEIGHT)));
            final String numberOfPoints = "" + points.size();
            final int widthVal = getFontMetricsPlain().stringWidth(numberOfPoints);
            add(GuiBuilder.buildLabel(numberOfPoints, getFontPlain(),
                    new Rectangle(widthLbl, getPnlY(), widthVal, LHEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    private void addNumberOfTrips(final Integer numberOfTrips, final int widthLbl) {
        if (numberOfTrips != null) {
            add(GuiBuilder.buildLabel(MissingGeometryGuiConfig.getInstance().getLblTripCount(), getFontBold(),
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LHEIGHT)));
            final int widthVal = getFontMetricsPlain().stringWidth(numberOfTrips.toString());
            add(GuiBuilder.buildLabel(numberOfTrips.toString(), getFontPlain(),
                    new Rectangle(widthLbl, getPnlY(), widthVal, LHEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }
}