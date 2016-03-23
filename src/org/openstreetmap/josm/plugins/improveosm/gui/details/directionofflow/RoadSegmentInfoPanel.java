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

import java.awt.Rectangle;
import org.openstreetmap.josm.plugins.improveosm.entity.OnewayConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadType;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.BasicPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.Formatter;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.GuiBuilder;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;


/**
 * Displays the information of a selected {@code RoadSegment}
 *
 * @author Beata
 * @version $Revision$
 */
public class RoadSegmentInfoPanel extends BasicPanel<RoadSegment> {

    private static final long serialVersionUID = 4836239803613790133L;


    /**
     * Builds an empty panel
     */
    public RoadSegmentInfoPanel() {
        super();
    }


    @Override
    public void createComponents(final RoadSegment roadSegment) {
        final DirectionOfFlowGuiConfig dofGuiCnf = DirectionOfFlowGuiConfig.getInstance();
        final GuiConfig guiCnf = GuiConfig.getInstance();
        final int widthLbl = getMaxWidth(getFontMetricsBold(), dofGuiCnf.getLblTrips(), dofGuiCnf.getLblType(),
                guiCnf.getLblStatus(), guiCnf.getLblConfidence());

        addPercentage(roadSegment.getPercentOfTrips());
        addTotalTrips(roadSegment.getNumberOfTrips(), widthLbl);
        addRoadType(roadSegment.getType(), widthLbl);
        addStatus(roadSegment.getStatus(), widthLbl);
        addConfidence(roadSegment.getConfidenceLevel(), widthLbl);
    }

    private void addPercentage(final Double percentage) {
        if (percentage != null) {
            final String lbl =
                    Formatter.formatDouble(percentage, true) + DirectionOfFlowGuiConfig.getInstance().getLblProcent();
            final int widthLbl = getFontMetricsBold().stringWidth(lbl.toString());
            add(GuiBuilder.buildLabel(lbl, getFontBold(), new Rectangle(RECT_X, RECT_Y, widthLbl, LHEIGHT)));
            incrementPnlY();
        }
    }

    private void addTotalTrips(final Integer numberOfTrips, final int widthLbl) {
        if (numberOfTrips != null) {
            add(GuiBuilder.buildLabel(DirectionOfFlowGuiConfig.getInstance().getLblTrips(), getFontBold(),
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LHEIGHT)));
            final int widthVal = getFontMetricsPlain().stringWidth(numberOfTrips.toString());
            add(GuiBuilder.buildLabel(numberOfTrips.toString(), getFontPlain(),
                    new Rectangle(widthLbl, getPnlY(), widthVal, LHEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    private void addRoadType(final RoadType type, final int widthLbl) {
        if (type != null) {
            add(GuiBuilder.buildLabel(DirectionOfFlowGuiConfig.getInstance().getLblType(), getFontBold(),
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LHEIGHT)));
            final int widthVal = getFontMetricsPlain().stringWidth(type.toString());
            add(GuiBuilder.buildLabel(type.toString().toLowerCase(), getFontPlain(),
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
            add(GuiBuilder.buildLabel(status.name().toLowerCase(), getFontPlain(),
                    new Rectangle(widthLbl, getPnlY(), widthVal, LHEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    private void addConfidence(final OnewayConfidenceLevel confidence, final int widthLbl) {
        if (confidence != null) {
            add(GuiBuilder.buildLabel(GuiConfig.getInstance().getLblConfidence(), getFontBold(),
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LHEIGHT)));
            final int widthVal = getFontMetricsPlain().stringWidth(confidence.longDisplayName());
            add(GuiBuilder.buildLabel(confidence.longDisplayName(), getFontPlain(),
                    new Rectangle(widthLbl, getPnlY(), widthVal, LHEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }
}