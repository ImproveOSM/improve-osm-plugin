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

import static org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder.BOLD_12;
import static org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder.PLAIN_12;
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
import org.openstreetmap.josm.plugins.improveosm.util.cnf.MissingGeoGuiConfig;


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
        final int widthLbl = getMaxWidth(FM_BOLD_12, getGuiCnf().getLblType(), getGuiCnf().getLblPointCount(),
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
            add(GuiBuilder.buildLabel(getGuiCnf().getLblId(), BOLD_12,
                    new Rectangle(RECT_X, RECT_Y, widthLbl, LHEIGHT)));
            final int widthVal = FM_PLAIN_12.stringWidth(tile.toString());
            add(GuiBuilder.buildLabel(tile.toString(), PLAIN_12, new Rectangle(widthLbl, RECT_Y, widthVal, LHEIGHT)));
            pnlWidth = pnlWidth + widthLbl + widthVal;
            y = RECT_Y + LHEIGHT;
        }
    }

    private void addType(final TileType type, final int widthLbl) {
        if (type != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblType(), BOLD_12, new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final int widthVal = FM_PLAIN_12.stringWidth(type.toString());
            add(GuiBuilder.buildLabel(type.toString(), PLAIN_12, new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private void addStatus(final Status status, final int widthLbl) {
        if (status != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblStatus(), BOLD_12,
                    new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final int widthVal = FM_PLAIN_12.stringWidth(status.name());
            add(GuiBuilder.buildLabel(status.name(), PLAIN_12, new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private void addTimestamp(final Long timestamp, final int widthLbl) {
        if (timestamp != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblTimestamp(), BOLD_12,
                    new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final String timestampStr = Formatter.formatTimestamp(timestamp);
            final int widthVal = FM_PLAIN_12.stringWidth(timestampStr);
            add(GuiBuilder.buildLabel(timestampStr, PLAIN_12, new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private void addNumberOfPoints(final List<LatLon> points, final int widthLbl) {
        if (points != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblPointCount(), BOLD_12,
                    new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final String numberOfPoints = "" + points.size();
            final int widthVal = FM_PLAIN_12.stringWidth(numberOfPoints);
            add(GuiBuilder.buildLabel(numberOfPoints, PLAIN_12, new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private void addNumberOfTrips(final Integer numberOfTrips, final int widthLbl) {
        if (numberOfTrips != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblTripCount(), BOLD_12,
                    new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final int widthVal = FM_PLAIN_12.stringWidth(numberOfTrips.toString());
            add(GuiBuilder.buildLabel(numberOfTrips.toString(), PLAIN_12,
                    new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private MissingGeoGuiConfig getGuiCnf() {
        return MissingGeoGuiConfig.getInstance();
    }
}