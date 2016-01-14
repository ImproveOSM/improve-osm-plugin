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
package org.openstreetmap.josm.plugins.improveosm.gui.details.common;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.openstreetmap.josm.plugins.improveosm.argument.SearchFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;


/**
 * Defines common filters that can be applied to any data layer. Specific filter panel's should extend this class.
 *
 * @author Beata
 * @version $Revision$
 */
public abstract class BasicFilterPanel extends JPanel {

    private static final long serialVersionUID = 1285306291492729387L;

    /* UI components */
    private JRadioButton rbStatusOpen;
    private JRadioButton rbStatusSolved;
    private JRadioButton rbStatusInvalid;
    private ButtonGroup btnGroupStatus;


    /**
     * Builds a new filter panel with the given arguments.
     *
     * @param filter the currently selected {@code SearchFilter}s
     * @param statusLbl the status filter label corresponding to the data layer
     */
    public BasicFilterPanel(final SearchFilter filter, final String statusLbl) {
        super(new GridBagLayout());
        addStatusFilter(statusLbl, filter.getStatus());
    }


    private void addStatusFilter(final String statusLbl, final Status status) {
        add(GuiBuilder.buildLabel(statusLbl, getFont().deriveFont(Font.BOLD, GuiBuilder.FONT_SIZE), null),
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

    /**
     * Selects the status filter UI element corresponding to the given status.
     *
     * @param status a {@code Status} object
     */
    public void selectStatus(final Status status) {
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

    /**
     * Returns the selected filters.
     *
     * @return a {@code SearchFilter} object
     */
    public SearchFilter selectedFilters() {
        Status status = null;
        if (btnGroupStatus.getSelection() != null) {
            status = Status.valueOf(btnGroupStatus.getSelection().getActionCommand());
        }
        return new SearchFilter(status);
    }

    /**
     * Resets the filters to it's default value.
     */
    protected void resetFilters() {
        selectStatus(SearchFilter.DEFAULT.getStatus());
    }


    /* Holds UI constraints */
    private static final class Constraints {

        private Constraints() {}

        private static final GridBagConstraints LBL_STATUS = new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(7, 5, 3, 5), 0, 0);
        private static final GridBagConstraints RB_OPEN = new GridBagConstraints(1, 0, 1, 1, 1, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
        private static final GridBagConstraints RB_SOLVED = new GridBagConstraints(2, 0, 1, 1, 1, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 5), 0, 0);
        private static final GridBagConstraints RB_INVALID = new GridBagConstraints(3, 0, 1, 1, 1, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 5), 0, 0);
    }
}