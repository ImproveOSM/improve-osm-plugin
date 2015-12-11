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

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.EnumSet;
import javax.swing.JCheckBox;
import org.openstreetmap.josm.plugins.improveosm.argument.TurnRestrictionFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.OnewayConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.BasicFilterPanel;
import org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.TurnRestrictionGuiConfig;


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


    /**
     * Builds a new panel displaying the TurnRestrictionLayer filters.
     *
     * @param filter the currently selected filters
     */
    public FilterPanel(final TurnRestrictionFilter filter) {
        super(filter, TurnRestrictionGuiConfig.getInstance().getDlgFilterStatusLbl());

        add(GuiBuilder.buildLabel(TurnRestrictionGuiConfig.getInstance().getDlgFilterConfidenceLbl(),
                getFont().deriveFont(Font.BOLD), null), Constraints.LBL_CONFIDENCE);
        cbbConfidenceC1 = GuiBuilder.buildCheckBox(TurnConfidenceLevel.C1.shortDisplayName(),
                OnewayConfidenceLevel.C1.name(), getBackground());
        cbbConfidenceC2 = GuiBuilder.buildCheckBox(OnewayConfidenceLevel.C2.shortDisplayName(),
                OnewayConfidenceLevel.C2.name(), getBackground());
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

    private void selectConfidence(final EnumSet<TurnConfidenceLevel> confidenceLevels) {
        if (confidenceLevels != null) {
            boolean selected = confidenceLevels.contains(OnewayConfidenceLevel.C1);
            cbbConfidenceC1.setSelected(selected);
            selected = confidenceLevels.contains(OnewayConfidenceLevel.C2);
            cbbConfidenceC2.setSelected(selected);
        } else {
            cbbConfidenceC1.setSelected(false);
            cbbConfidenceC2.setSelected(false);
        }
    }

    private static final class Constraints {

        private Constraints() {}

        private static final GridBagConstraints LBL_CONFIDENCE = new GridBagConstraints(0, 1, 1, 1, 1, 1,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 3, 5), 0, 0);
        private static final GridBagConstraints CBB_C1 = new GridBagConstraints(1, 1, 1, 1, 1, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
        private static final GridBagConstraints CBB_C2 = new GridBagConstraints(2, 1, 1, 1, 1, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 5), 0, 0);
    }
}