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
import java.awt.GridBagLayout;
import java.util.EnumSet;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.openstreetmap.josm.plugins.improveosm.argument.OnewayFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.OnewayConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 *
 * @author Beata
 * @version $Revision$
 */
class FilterPanel extends JPanel {

    private static final long serialVersionUID = -1119298746308645030L;

    /* UI components */
    private JRadioButton rbStatusOpen;
    private JRadioButton rbStatusSolved;
    private JRadioButton rbStatusInvalid;
    private ButtonGroup btnGroupStatus;
    private JCheckBox cbbConfidenceC1;
    private JCheckBox cbbConfidenceC2;
    private JCheckBox cbbConfidenceC3;


    FilterPanel() {
        super(new GridBagLayout());
        final OnewayFilter filter = PreferenceManager.getInstance().loadOnewayFilter();
        addStatusFilter(filter.getStatus());
        addConfidenceFilter(filter.getConfidenceLevels());
    }

    private void addStatusFilter(final Status status) {
        add(GuiBuilder.buildLabel(DirectionOfFlowGuiConfig.getInstance().getDlgFilterStatusLbl(), BOLD_12, null),
                Constraints.LBL_STATUS);
        rbStatusOpen =
                GuiBuilder.buildRadioButton(Status.OPEN.name().toLowerCase(), Status.OPEN.toString(), getBackground());
        rbStatusSolved = GuiBuilder.buildRadioButton(Status.SOLVED.name().toLowerCase(), Status.SOLVED.toString(),
                getBackground());
        rbStatusInvalid = GuiBuilder.buildRadioButton(Status.INVALID.name().toLowerCase(), Status.INVALID.toString(),
                getBackground());
        btnGroupStatus = GuiBuilder.buildButtonGroup(rbStatusOpen, rbStatusSolved, rbStatusInvalid);
        selectStatus(status);
        add(rbStatusOpen, Constraints.RB_OPEN);
        add(rbStatusSolved, Constraints.RB_SOLVED);
        add(rbStatusInvalid, Constraints.RB_INVALID);
    }

    private void addConfidenceFilter(final EnumSet<OnewayConfidenceLevel> confidenceLevels) {
        add(GuiBuilder.buildLabel(DirectionOfFlowGuiConfig.getInstance().getDlgFilterConfidenceLbl(), BOLD_12, null),
                Constraints.LBL_CONFIDENCE);
        cbbConfidenceC1 = GuiBuilder.buildCheckBox(OnewayConfidenceLevel.C1.shortDisplayName(),
                OnewayConfidenceLevel.C1.name(), getBackground());
        cbbConfidenceC2 = GuiBuilder.buildCheckBox(OnewayConfidenceLevel.C2.shortDisplayName(),
                OnewayConfidenceLevel.C2.name(), getBackground());
        cbbConfidenceC3 = GuiBuilder.buildCheckBox(OnewayConfidenceLevel.C3.shortDisplayName(),
                OnewayConfidenceLevel.C3.name(), getBackground());
        selectConfidence(confidenceLevels);
        add(cbbConfidenceC1, Constraints.CBB_C1);
        add(cbbConfidenceC2, Constraints.CBB_C2);
        add(cbbConfidenceC3, Constraints.CBB_C3);
    }

    /**
     * Returns the selected filters.
     *
     * @return a {@code SearchFilter} object
     */
    OnewayFilter selectedFilters() {
        Status status = null;
        if (btnGroupStatus.getSelection() != null) {
            status = Status.valueOf(btnGroupStatus.getSelection().getActionCommand());
        }
        final EnumSet<OnewayConfidenceLevel> confidenceLevels = EnumSet.noneOf(OnewayConfidenceLevel.class);
        if (cbbConfidenceC1.isSelected()) {
            confidenceLevels.add(OnewayConfidenceLevel.C1);
        }
        if (cbbConfidenceC2.isSelected()) {
            confidenceLevels.add(OnewayConfidenceLevel.C2);
        }
        if (cbbConfidenceC3.isSelected()) {
            confidenceLevels.add(OnewayConfidenceLevel.C3);
        }
        return status == null && confidenceLevels.isEmpty() ? null : new OnewayFilter(status, confidenceLevels);
    }

    /**
     * Resets the search filters to the default ones.
     */
    void resetFilters() {
        selectStatus(OnewayFilter.DEFAULT.getStatus());
        selectConfidence(OnewayFilter.DEFAULT.getConfidenceLevels());
    }

    private void selectStatus(final Status status) {
        if (status != null) {
            switch (status) {
                case OPEN:
                    rbStatusOpen.setSelected(true);
                    break;
                case SOLVED:
                    rbStatusSolved.setSelected(true);
                    break;
                default:
                    rbStatusInvalid.setSelected(true);
                    break;
            }
        } else {
            btnGroupStatus.clearSelection();
        }
    }

    private void selectConfidence(final EnumSet<OnewayConfidenceLevel> confidenceLevels) {
        if (confidenceLevels != null) {
            boolean selected = confidenceLevels.contains(OnewayConfidenceLevel.C1);
            cbbConfidenceC1.setSelected(selected);
            selected = confidenceLevels.contains(OnewayConfidenceLevel.C2);
            cbbConfidenceC2.setSelected(selected);
            selected = confidenceLevels.contains(OnewayConfidenceLevel.C3);
            cbbConfidenceC3.setSelected(selected);
        } else {
            cbbConfidenceC1.setSelected(false);
            cbbConfidenceC2.setSelected(false);
            cbbConfidenceC3.setSelected(false);
        }
    }
}