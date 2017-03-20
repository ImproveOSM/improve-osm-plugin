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

import java.awt.ComponentOrientation;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.improveosm.entity.OnewayConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import com.telenav.josm.common.formatter.DecFormat;
import com.telenav.josm.common.formatter.EntityFormatter;
import com.telenav.josm.common.gui.BasicInfoPanel;
import com.telenav.josm.common.gui.GuiBuilder;


/**
 * Displays the information of a selected {@code RoadSegment}
 *
 * @author Beata
 * @version $Revision$
 */
public class RoadSegmentInfoPanel extends BasicInfoPanel<RoadSegment> {

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
        final FontMetrics fm = Main.map.mapView.getGraphics().getFontMetrics(getFontBold());
        final int widthLbl = getMaxWidth(fm, dofGuiCnf.getLblTrips(), dofGuiCnf.getLblType(), guiCnf.getLblStatus(),
                guiCnf.getLblConfidence());

        addPercentage(roadSegment.getPercentOfTrips());
        addTotalTrips(roadSegment.getNumberOfTrips(), widthLbl);
        addRoadType(roadSegment.getType(), widthLbl);
        addStatus(roadSegment.getStatus(), widthLbl);
        addConfidence(roadSegment.getConfidenceLevel(), widthLbl);
    }

    private void addPercentage(final Double percentage) {
        if (percentage != null) {
            final String lbl = EntityFormatter.formatDouble(percentage, true, DecFormat.SHORT)
                    + DirectionOfFlowGuiConfig.getInstance().getLblProcent();
            final int widthLbl = Main.map.mapView.getGraphics().getFontMetrics(getFontBold()).stringWidth(lbl);
            add(GuiBuilder.buildLabel(lbl, getFontBold(), ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                    SwingConstants.TOP, new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            incrementPnlY();
        }
    }

    private void addTotalTrips(final Integer numberOfTrips, final int widthLbl) {
        if (numberOfTrips != null) {
            add(GuiBuilder.buildLabel(DirectionOfFlowGuiConfig.getInstance().getLblTrips(), getFontBold(),
                    ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            final int widthVal =
                    Main.map.mapView.getGraphics().getFontMetrics(getFontPlain()).stringWidth(numberOfTrips.toString());
            add(GuiBuilder.buildLabel(numberOfTrips.toString(), getFontPlain(), ComponentOrientation.LEFT_TO_RIGHT,
                    SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    private void addRoadType(final String type, final int widthLbl) {
        if (type != null) {
            add(GuiBuilder.buildLabel(DirectionOfFlowGuiConfig.getInstance().getLblType(), getFontBold(),
                    ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            final int widthVal = Main.map.mapView.getGraphics().getFontMetrics(getFontPlain()).stringWidth(type);
            add(GuiBuilder.buildLabel(type.toLowerCase(), getFontPlain(), ComponentOrientation.LEFT_TO_RIGHT,
                    SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    private void addStatus(final Status status, final int widthLbl) {
        if (status != null) {
            add(GuiBuilder.buildLabel(GuiConfig.getInstance().getLblStatus(), getFontBold(),
                    ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            final int widthVal =
                    Main.map.mapView.getGraphics().getFontMetrics(getFontPlain()).stringWidth(status.name());
            add(GuiBuilder.buildLabel(status.name().toLowerCase(), getFontPlain(), ComponentOrientation.LEFT_TO_RIGHT,
                    SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    private void addConfidence(final OnewayConfidenceLevel confidence, final int widthLbl) {
        if (confidence != null) {
            add(GuiBuilder.buildLabel(GuiConfig.getInstance().getLblConfidence(), getFontBold(),
                    ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            final int widthVal =
                    Main.map.mapView.getGraphics().getFontMetrics(getFontPlain()).stringWidth(confidence.toString());
            add(GuiBuilder.buildLabel(confidence.toString(), getFontPlain(), ComponentOrientation.LEFT_TO_RIGHT,
                    SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }
}