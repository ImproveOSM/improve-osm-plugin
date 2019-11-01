/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.turnrestrictions;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.Formatter;
import org.openstreetmap.josm.plugins.improveosm.observer.TurnRestrictionSelectionObserver;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.TurnRestrictionGuiConfig;
import com.telenav.josm.common.gui.BasicInfoPanel;
import com.telenav.josm.common.gui.builder.LabelBuilder;


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
            tblTurnRestrictions.updateData(null);
            super.updateData(null);
        } else if (!tblTurnRestrictions.contains(turnRestriction)) {
            super.updateData(turnRestriction);
        }
    }

    public void registerSelectionObserver(final TurnRestrictionSelectionObserver observer) {
        tblTurnRestrictions.addObserver(observer);
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
            final FontMetrics fm = getFontMetrics(getFont().deriveFont(Font.BOLD));
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

    private void addTurnRestrictionsTable(final List<TurnRestriction> turnRestrictions) {
        add(LabelBuilder.build(TurnRestrictionGuiConfig.getInstance().getTblTitle(), Font.BOLD,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP), BorderLayout.NORTH);

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
            final int widthLbl = getFontMetrics(getFont().deriveFont(Font.BOLD)).stringWidth(lbl);
            add(LabelBuilder.build(lbl, Font.BOLD, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                    SwingConstants.TOP, new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            incrementPnlY();
        }
    }

    private void addLastSegmentTrips(final Integer trips) {
        if (trips != null) {
            final String lbl = trips + " " + TurnRestrictionGuiConfig.getInstance().getLblLastSegmentTrips();
            final int widthLbl = getFontMetrics(getFont().deriveFont(Font.BOLD)).stringWidth(lbl);
            add(LabelBuilder.build(lbl, Font.BOLD, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                    SwingConstants.TOP, new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            incrementPnlY();
        }
    }

    private void addStatus(final Status status, final int widthLbl) {
        if (status != null) {
            add(LabelBuilder.build(GuiConfig.getInstance().getLblStatus(), Font.BOLD,
                    ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            final int widthVal = getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(status.name());
            add(LabelBuilder.build(status.name().toLowerCase(), Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT,
                    SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    private void addTurnType(final String type, final int widthLbl) {
        if (type != null) {
            add(LabelBuilder.build(TurnRestrictionGuiConfig.getInstance().getLblType(), Font.BOLD,
                    ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            final ImageIcon icon = TurnTypeIconFactory.getInstance().getIcon(type);
            final int widthVal =
                    getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(type) + icon.getIconWidth();
            add(LabelBuilder.build(Formatter.formatTurnType(type), icon, Font.PLAIN,
                    new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }

    private void addTurnConfidence(final TurnConfidenceLevel confidence, final int widthLbl) {
        if (confidence != null) {
            add(LabelBuilder.build(GuiConfig.getInstance().getLblConfidence(), Font.BOLD,
                    ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(RECT_X, getPnlY(), widthLbl, LINE_HEIGHT)));
            final int widthVal = getFontMetrics(getFont().deriveFont(Font.PLAIN)).stringWidth(confidence.toString());
            add(LabelBuilder.build(confidence.toString(), Font.PLAIN, ComponentOrientation.LEFT_TO_RIGHT,
                    SwingConstants.LEFT, SwingConstants.TOP,
                    new Rectangle(widthLbl, getPnlY(), widthVal, LINE_HEIGHT)));
            setPnlWidth(widthLbl + widthVal);
            incrementPnlY();
        }
    }
}