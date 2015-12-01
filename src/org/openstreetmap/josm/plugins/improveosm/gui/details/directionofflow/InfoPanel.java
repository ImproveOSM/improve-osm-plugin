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
package org.openstreetmap.josm.plugins.improveosm.gui.details.directionofflow;

import static org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder.BOLD_12;
import static  org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder.PLAIN_12;
import java.awt.Rectangle;
import org.openstreetmap.josm.plugins.improveosm.entity.OnewayConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadType;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.gui.details.BasicPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.Formatter;
import org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;

/**
 *
 * @author Beata
 * @version $Revision$
 */
class InfoPanel extends BasicPanel<RoadSegment> {

    private static final long serialVersionUID = 4836239803613790133L;
    private static final int WIDTH_MARGIN = 5;
    private int y = 0;
    private int pnlWidth = 0;


    @Override
    public void createComponents(final RoadSegment roadSegment) {
        y = 0;
        pnlWidth = 0;
        final int widthLbl = getMaxWidth(FM_BOLD_12, getGuiCnf().getLblTrips(), getGuiCnf().getLblId(),
                getGuiCnf().getLblType(), getGuiCnf().getLblStatus(), getGuiCnf().getLblConfidence());

        addPercentage(roadSegment.getPercentOfTrips());
        addTotalTrips(roadSegment.getNumberOfTrips(), widthLbl);
        addIdentifier(roadSegment, widthLbl);
        addRoadType(roadSegment.getType(), widthLbl);
        addStatus(roadSegment.getStatus(), widthLbl);
        addConfidence(roadSegment.getConfidenceLevel(), widthLbl);
    }

    private void addPercentage(final Double percentage) {
        if (percentage != null) {
            final String lbl = Formatter.formatDouble(percentage, true) + getGuiCnf().getLblProcent();
            final int widthLbl = FM_BOLD_12.stringWidth(lbl.toString());
            add(GuiBuilder.buildLabel(lbl, BOLD_12, new Rectangle(RECT_X, RECT_Y, widthLbl, LHEIGHT)));
            y = RECT_Y + LHEIGHT;
        }
    }

    private void addTotalTrips(final Integer numberOfTrips, final int widthLbl) {
        if (numberOfTrips != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblTrips(), BOLD_12, new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final int widthVal = FM_PLAIN_12.stringWidth(numberOfTrips.toString());
            add(GuiBuilder.buildLabel(numberOfTrips.toString(), PLAIN_12,
                    new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private void addIdentifier(final RoadSegment roadSegment, final int widthLbl) {
        if (roadSegment.getWayId() != null && roadSegment.getFromNodeId() != null
                && roadSegment.getToNodeId() != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblId(), BOLD_12, new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final int widthVal = FM_PLAIN_12.stringWidth(roadSegment.toString()) + WIDTH_MARGIN;
            add(GuiBuilder.buildLabel(roadSegment.toString(), PLAIN_12, new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = pnlWidth + widthLbl + widthVal;
            y = y + LHEIGHT;
        }
    }

    private void addRoadType(final RoadType type, final int widthLbl) {
        if (type != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblType(), BOLD_12, new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final int widthVal = FM_PLAIN_12.stringWidth(type.toString());
            add(GuiBuilder.buildLabel(type.toString().toLowerCase(), PLAIN_12,
                    new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private void addStatus(final Status status, final int widthLbl) {
        if (status != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblStatus(), BOLD_12,
                    new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final int widthVal = FM_PLAIN_12.stringWidth(status.name());
            add(GuiBuilder.buildLabel(status.name().toLowerCase(), PLAIN_12,
                    new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private void addConfidence(final OnewayConfidenceLevel confidence, final int widthLbl) {
        if (confidence != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblConfidence(), BOLD_12,
                    new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final int widthVal = FM_PLAIN_12.stringWidth(confidence.longDisplayName());
            add(GuiBuilder.buildLabel(confidence.longDisplayName(), PLAIN_12,
                    new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private DirectionOfFlowGuiConfig getGuiCnf() {
        return DirectionOfFlowGuiConfig.getInstance();
    }
}