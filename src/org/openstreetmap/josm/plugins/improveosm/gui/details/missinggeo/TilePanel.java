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
import org.openstreetmap.josm.plugins.improveosm.gui.details.BasicPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.Formatter;
import org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.MissingGeometryGuiConfig;


/**
 *
 * @author Beata
 * @version $Revision$
 */
class TilePanel extends BasicPanel<Tile> {

    private static final long serialVersionUID = 5842933383198993565L;
    private int y = 0;
    private int pnlWidth = 0;

    @Override
    public void createComponents(final Tile tile) {
        y = 0;
        pnlWidth = 0;
        final int widthLbl = getMaxWidth(getFontMetricsBold(), getGuiCnf().getLblType(), getGuiCnf().getLblPointCount(),
                getGuiCnf().getLblTripCount(), getGuiCnf().getLblStatus(), getGuiCnf().getLblTimestamp());

        addIdentifier(tile, widthLbl);
        addType(tile.getType(), widthLbl);
        addStatus(tile.getStatus(), widthLbl);
        addTimestamp(tile.getTimestamp(), widthLbl);
        addNumberOfTrips(tile.getNumberOfTrips(), widthLbl);
        addNumberOfPoints(tile.getPoints(), widthLbl);
        final int pnlHeight = y + SPACE_Y;
        setPreferredSize(new Dimension(pnlWidth + SPACE_Y, pnlHeight));
    }

    private void addIdentifier(final Tile tile, final int widthLbl) {
        if (tile.getX() != null && tile.getY() != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblId(), getFontBold(),
                    new Rectangle(RECT_X, RECT_Y, widthLbl, LHEIGHT)));
            final int widthVal = getFontMetricsPlain().stringWidth(tile.toString());
            add(GuiBuilder.buildLabel(tile.toString(), getFontPlain(),
                    new Rectangle(widthLbl, RECT_Y, widthVal, LHEIGHT)));
            pnlWidth = pnlWidth + widthLbl + widthVal;
            y = RECT_Y + LHEIGHT;
        }
    }

    private void addType(final TileType type, final int widthLbl) {
        if (type != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblType(), getFontBold(),
                    new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final int widthVal = getFontMetricsPlain().stringWidth(type.toString());
            add(GuiBuilder.buildLabel(type.toString(), getFontPlain(), new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private void addStatus(final Status status, final int widthLbl) {
        if (status != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblStatus(), getFontBold(),
                    new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final int widthVal = getFontMetricsPlain().stringWidth(status.name());
            add(GuiBuilder.buildLabel(status.name(), getFontPlain(), new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private void addTimestamp(final Long timestamp, final int widthLbl) {
        if (timestamp != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblTimestamp(), getFontBold(),
                    new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final String timestampStr = Formatter.formatTimestamp(timestamp);
            final int widthVal = getFontMetricsPlain().stringWidth(timestampStr);
            add(GuiBuilder.buildLabel(timestampStr, getFontPlain(), new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private void addNumberOfPoints(final List<LatLon> points, final int widthLbl) {
        if (points != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblPointCount(), getFontBold(),
                    new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final String numberOfPoints = "" + points.size();
            final int widthVal = getFontMetricsPlain().stringWidth(numberOfPoints);
            add(GuiBuilder.buildLabel(numberOfPoints, getFontPlain(), new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private void addNumberOfTrips(final Integer numberOfTrips, final int widthLbl) {
        if (numberOfTrips != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblTripCount(), getFontBold(),
                    new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final int widthVal = getFontMetricsPlain().stringWidth(numberOfTrips.toString());
            add(GuiBuilder.buildLabel(numberOfTrips.toString(), getFontPlain(),
                    new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private MissingGeometryGuiConfig getGuiCnf() {
        return MissingGeometryGuiConfig.getInstance();
    }
}