/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.turnrestrictions;

import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.EnumSet;
import java.util.Set;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.plugins.improveosm.argument.TurnRestrictionFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.BasicFilterPanel;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import com.telenav.josm.common.gui.builder.CheckBoxBuilder;
import com.telenav.josm.common.gui.builder.LabelBuilder;


/**
 * Displays the possible data filters: status, confidence level.
 *
 * @author Beata
 * @version $Revision$
 */
class FilterPanel extends BasicFilterPanel {

    private static final long serialVersionUID = 4547998379091364839L;

    /* UI components */
    private final JCheckBox cbbConfidenceC1;
    private final JCheckBox cbbConfidenceC2;


    FilterPanel(final TurnRestrictionFilter filter) {
        super(filter, GuiConfig.getInstance().getLblStatus());

        add(LabelBuilder.build(GuiConfig.getInstance().getLblConfidence(), Font.BOLD,
                ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT, SwingConstants.TOP),
                Constraints.LBL_CONFIDENCE);
        cbbConfidenceC1 = CheckBoxBuilder.build(TurnConfidenceLevel.C1.toString(), Font.PLAIN, getBackground(), false);
        cbbConfidenceC2 = CheckBoxBuilder.build(TurnConfidenceLevel.C2.toString(), Font.PLAIN, getBackground(), false);
        selectConfidence(filter.getConfidenceLevels());
        add(cbbConfidenceC1, Constraints.CBB_C1);
        add(cbbConfidenceC2, Constraints.CBB_C2);
    }


    @Override
    public TurnRestrictionFilter selectedFilters() {
        final Status status = super.selectedFilters().getStatus();
        final EnumSet<TurnConfidenceLevel> confidenceLevels = EnumSet.noneOf(TurnConfidenceLevel.class);
        if (cbbConfidenceC1.isSelected()) {
            confidenceLevels.add(TurnConfidenceLevel.C1);
        }
        if (cbbConfidenceC2.isSelected()) {
            confidenceLevels.add(TurnConfidenceLevel.C2);
        }
        return status == null && confidenceLevels.isEmpty() ? null
                : new TurnRestrictionFilter(status, confidenceLevels);
    }

    @Override
    public void resetFilters() {
        super.resetFilters();
        selectConfidence(TurnRestrictionFilter.DEFAULT.getConfidenceLevels());
    }

    private void selectConfidence(final Set<TurnConfidenceLevel> confidenceLevels) {
        if (confidenceLevels != null) {
            boolean selected = confidenceLevels.contains(TurnConfidenceLevel.C1);
            cbbConfidenceC1.setSelected(selected);
            selected = confidenceLevels.contains(TurnConfidenceLevel.C2);
            cbbConfidenceC2.setSelected(selected);
        } else {
            cbbConfidenceC1.setSelected(false);
            cbbConfidenceC2.setSelected(false);
        }
    }


    private static final class Constraints {

        private static final GridBagConstraints LBL_CONFIDENCE = new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);
        private static final GridBagConstraints CBB_C1 = new GridBagConstraints(1, 1, 1, 1, 1, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
        private static final GridBagConstraints CBB_C2 = new GridBagConstraints(2, 1, 1, 1, 1, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 5), 0, 0);

        private Constraints() {}
    }
}