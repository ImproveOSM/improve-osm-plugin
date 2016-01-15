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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.JPanel;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.gui.details.BasicPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.Formatter;
import org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder;
import org.openstreetmap.josm.plugins.improveosm.observer.TurnRestrictionSelectionObserver;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.TurnRestrictionGuiConfig;


/**
 * Displays the information of the selected element.
 *
 * @author Beata
 * @version $Revision$
 */
final class InfoPanel extends BasicPanel<TurnRestriction> {

    private static final long serialVersionUID = -5437372726391531598L;
    private static final int GAP = 10;
    private int y = 0;
    private int pnlWidth = 0;

    private final TurnRestrictionTable tblTurnRestrictions;


    InfoPanel() {
        super();
        setName(getGuiCnf().getPnlInfoTitle());
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
        y = 0;
        pnlWidth = 0;
        if (turnRestriction.getTurnRestrictions() != null) {
            setLayout(new BorderLayout(GAP, GAP));
            addTurnRestrictionsTable(turnRestriction.getTurnRestrictions());
        } else {
            if (tblTurnRestrictions.getRowCount() > 1) {
                tblTurnRestrictions.updateData(null);
            }
            final int widthLbl = getMaxWidth(getFontMetricsBold(), getGuiCnf().getLblId(), getGuiCnf().getLblStatus(),
                    getGuiCnf().getLblType(), getGuiCnf().getLblConfidence(), getGuiCnf().getLblTrips());
            addIdentifier(turnRestriction.getId(), widthLbl);
            addStatus(turnRestriction.getStatus(), widthLbl);
            addTurnType(turnRestriction.getTurnType(), widthLbl);
            addTurnConfidence(turnRestriction.getConfidenceLevel(), widthLbl);
            addPasses(turnRestriction.getNumberOfPasses(), widthLbl);
        }
        setPreferredSize(new Dimension(pnlWidth + SPACE_Y, y));
    }

    void registerSelectionObserver(final TurnRestrictionSelectionObserver observer) {
        tblTurnRestrictions.addObserver(observer);
    }

    private void addTurnRestrictionsTable(final List<TurnRestriction> turnRestrictions) {
        add(GuiBuilder.buildLabel(getGuiCnf().getTblTitle(), Color.black, true), BorderLayout.NORTH);

        tblTurnRestrictions.updateData(turnRestrictions);
        tblTurnRestrictions.setPreferredScrollableViewportSize(tblTurnRestrictions.getPreferredSize());
        final JPanel pnlTable = new JPanel(new BorderLayout());
        pnlTable.add(tblTurnRestrictions.getTableHeader(), BorderLayout.NORTH);
        pnlTable.add(tblTurnRestrictions, BorderLayout.CENTER);
        add(pnlTable, BorderLayout.CENTER);
        y = LHEIGHT + y + pnlTable.getHeight();
        pnlWidth = pnlTable.getPreferredSize().width + 2 * SPACE_Y;
    }

    private void addIdentifier(final String id, final int widthLbl) {
        if (id != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblId(), getFontBold(),
                    new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final int widthVal = getFontMetricsPlain().stringWidth(id);
            add(GuiBuilder.buildLabel(id, getFontPlain(), new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private void addStatus(final Status status, final int widthLbl) {
        if (status != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblStatus(), getFontBold(),
                    new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final int widthVal = getFontMetricsPlain().stringWidth(status.name());
            add(GuiBuilder.buildLabel(status.name().toLowerCase(), getFontPlain(),
                    new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private void addTurnType(final String type, final int widthLbl) {
        if (type != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblType(), getFontBold(),
                    new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final int widthVal = getFontMetricsPlain().stringWidth(type.toString());
            add(GuiBuilder.buildLabel(Formatter.formatTurnType(type), getFontPlain(),
                    new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private void addTurnConfidence(final TurnConfidenceLevel confidence, final int widthLbl) {
        if (confidence != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblConfidence(), getFontBold(),
                    new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final int widthVal = getFontMetricsPlain().stringWidth(confidence.longDisplayName());
            add(GuiBuilder.buildLabel(confidence.longDisplayName(), getFontPlain(),
                    new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private void addPasses(final Integer passes, final int widthLbl) {
        if (passes != null) {
            add(GuiBuilder.buildLabel(getGuiCnf().getLblTrips(), getFontBold(),
                    new Rectangle(RECT_X, y, widthLbl, LHEIGHT)));
            final int widthVal = getFontMetricsPlain().stringWidth(passes.toString());
            add(GuiBuilder.buildLabel(passes.toString(), getFontPlain(),
                    new Rectangle(widthLbl, y, widthVal, LHEIGHT)));
            pnlWidth = Math.max(pnlWidth, widthLbl + widthVal);
            y = y + LHEIGHT;
        }
    }

    private TurnRestrictionGuiConfig getGuiCnf() {
        return TurnRestrictionGuiConfig.getInstance();
    }
}