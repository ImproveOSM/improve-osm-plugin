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
package org.openstreetmap.josm.plugins.improveosm.gui.details.turnrestrictions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.Formatter;
import org.openstreetmap.josm.plugins.improveosm.observer.TurnRestrictionSelectionObserver;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.TurnRestrictionGuiConfig;
import com.telenav.josm.common.gui.BasicInfoPanel;
import com.telenav.josm.common.gui.GuiBuilder;


/**
 * Displays the information of the selected element.
 *
 * @author Beata
 * @version $Revision$
 */
public final class TurnRestrictionInfoPanel extends BasicInfoPanel<TurnRestriction> {

    private static final long serialVersionUID = -5437372726391531598L;
    private static final int GAP = 10;
    private static final int TABLE_WIDTH_SPACE = 2;
    private final TurnRestrictionTable tblTurnRestrictions;


    public TurnRestrictionInfoPanel() {
        tblTurnRestrictions = new TurnRestrictionTable();
    }


    @Override
    public void updateData(final TurnRestriction turnRestriction) {
        if (turnRestriction == null) {
            if (tblTurnRestrictions != null) {
                tblTurnRestrictions.updateData(null);
            }
            super.updateData(turnRestriction);
        } else if (!tblTurnRestrictions.contains(turnRestriction)) {
            super.updateData(turnRestriction);
        }
    }

    @Override
    public void createComponents(final TurnRestriction turnRestriction) {
        if (turnRestriction.getTurnRestrictions() != null) {
            setLayout(new BorderLayout(GAP, GAP));
            addTurnRestrictionsTable(turnRestriction.getTurnRestrictions());
        } else {
            if (tblTurnRestrictions.getRowCount() > 1) {
                tblTurnRestrictions.updateData(null);
            }
            final TurnRestrictionGuiConfig trGuiCnf = TurnRestrictionGuiConfig.getInstance();
            final GuiConfig guiCnf = GuiConfig.getInstance();
            final FontMetrics fm = Main.map.mapView.getGraphics().getFontMetrics(getFont().deriveFont(Font.BOLD));
            final int widthLbl =
                    getMaxWidth(fm, guiCnf.getLblStatus(), trGuiCnf.getLblType(), guiCnf.getLblConfidence());
            addFirstSegmentTrips(turnRestriction.getSegments().get(0).getNumberOfTrips());
            addLastSegmentTrips(turnRestriction.getNumberOfPasses());
            addStatus(turnRestriction.getStatus(), widthLbl);
            addTurnType(turnRestriction.getTurnType(), widthLbl);
            addTurnConfidence(turnRestriction.getConfidenceLevel(), widthLbl);
        }
        setPreferredSize(new Dimension(getPnlWidth() + SPACE_Y, getPnlY()));
    }

    public void registerSelectionObserver(final TurnRestrictionSelectionObserver observer) {
        tblTurnRestrictions.addObserver(observer);
    }

    private void addTurnRestrictionsTable(final List<TurnRestriction> turnRestrictions) {
        add(GuiBuilder.buildLabel(TurnRestrictionGuiConfig.getInstance().getTblTitle(), Color.black, Font.BOLD,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP, true), BorderLayout.NORTH);

        tblTurnRestrictions.updateData(turnRestrictions);
        tblTurnRestrictions.setPreferredScrollableViewportSize(tblTurnRestrictions.getPreferredSize());
        final JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.add(tblTurnRestrictions.getTableHeader(), BorderLayout.NORTH);
        pnlTable.add(tblTurnRestrictions, BorderLayout.CENTER);
        add(pnlTable, BorderLayout.CENTER);
        setPnlY(LINE_HEIGHT + pnlTable.getHeight());
        setPnlWidth(pnlTable.getPreferredSize().width + TABLE_WIDTH_SPACE * SPACE_Y);
    }

    private void addFirstSegmentTrips(final Integer trips) {
        if (trips != null) {
            final String lbl = trips + " " + TurnRestrictionGuiConfig.getInstance().getLblFirstSegmentTrips();
            final int widthLbl =
                    Main.map.mapView.getGraphics().getFontMetrics(getFont().deriveFont(Font.BOLD)).stringWidth(lbl);
            add(GuiBuilder.buildLabel(lbl, Font.BOLD, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                    SwingConstants.TOP, new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            incrementPnlY();
        }
    }

    private void addLastSegmentTrips(final Integer trips) {
        if (trips != null) {
            final String lbl = trips + " " + TurnRestrictionGuiConfig.getInstance().getLblLastSegmentTrips();
            final int widthLbl =
                    Main.map.mapView.getGraphics().getFontMetrics(getFont().deriveFont(Font.BOLD)).stringWidth(lbl);
            add(GuiBuilder.buildLabel(lbl, Font.BOLD, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                    SwingConstants.TOP, new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            incrementPnlY();
        }
    }

    private void addStatus(final Status status, final int widthLbl) {
        if (status != null) {
            add(GuiBuilder.buildLabel(GuiConfig.getInstance().getLblStatus(), Font.BOLD,
                    ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            final int widthVal = Main.map.mapView.getGraphics().getFontMetrics(getFont().deriveFont(Font.PLAIN))
                    .stringWidth(status.name());
            add(GuiBuilder.buildLabel(status.name().toLowerCase(), Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT,
                    SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    private void addTurnType(final String type, final int widthLbl) {
        if (type != null) {
            add(GuiBuilder.buildLabel(TurnRestrictionGuiConfig.getInstance().getLblType(), Font.BOLD,
                    ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            final ImageIcon icon = TurnTypeIconFactory.getInstance().getIcon(type);
            final int widthVal =
                    Main.map.mapView.getGraphics().getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(type)
                    + icon.getIconWidth();
            add(GuiBuilder.buildLabel(Formatter.formatTurnType(type), icon, Font.PLAIN,
                    new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    private void addTurnConfidence(final TurnConfidenceLevel confidence, final int widthLbl) {
        if (confidence != null) {
            add(GuiBuilder.buildLabel(GuiConfig.getInstance().getLblConfidence(), Font.BOLD,
                    ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            final int widthVal = Main.map.mapView.getGraphics().getFontMetrics(getFont().deriveFont(Font.PLAIN))
                    .stringWidth(confidence.toString());
            add(GuiBuilder.buildLabel(confidence.toString(), Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT,
                    SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }
}