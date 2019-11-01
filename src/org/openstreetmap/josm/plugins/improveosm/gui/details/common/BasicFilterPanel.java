/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.common;

import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import org.openstreetmap.josm.plugins.improveosm.argument.SearchFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import com.telenav.josm.common.gui.builder.ButtonBuilder;
import com.telenav.josm.common.gui.builder.LabelBuilder;


/**
 * Defines common filters that can be applied to any data layer. Specific filter panel's should extend this class.
 *
 * @author Beata
 * @version $Revision$
 */
public abstract class BasicFilterPanel extends JPanel {

    private static final long serialVersionUID = 1285306291492729387L;

    /* UI components */
    private final JRadioButton rbStatusOpen;
    private final JRadioButton rbStatusSolved;
    private final JRadioButton rbStatusInvalid;
    private final ButtonGroup btnGroupStatus;


    /**
     * Builds a new filter panel with the given arguments.
     *
     * @param filter the currently selected {@code SearchFilter}s
     * @param statusLbl the status filter label corresponding to the data layer
     */
    public BasicFilterPanel(final SearchFilter filter, final String statusLbl) {
        super(new GridBagLayout());

        add(LabelBuilder.build(statusLbl, Font.BOLD, ComponentOrientation.LEFT_TO_RIGHT, SwingConstants.LEFT,
                SwingConstants.TOP), Constraints.LBL_STATUS);
        rbStatusOpen = ButtonBuilder.build(Status.OPEN.name().toLowerCase(), Status.OPEN.toString(), Font.PLAIN,
                getBackground(), false);
        rbStatusSolved = ButtonBuilder.build(Status.SOLVED.name().toLowerCase(), Status.SOLVED.toString(), Font.PLAIN,
                getBackground(), false);
        rbStatusInvalid = ButtonBuilder.build(Status.INVALID.name().toLowerCase(), Status.INVALID.toString(),
                Font.PLAIN, getBackground(), false);

        btnGroupStatus = ButtonBuilder.build(rbStatusOpen, rbStatusSolved, rbStatusInvalid);
        selectStatus(filter.getStatus());
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
        selectStatus(SearchFilter.DEFAULT_SEARCH_FILTER.getStatus());
    }

    /**
     * Holds UI constraints
     *
     * @author beataj
     * @version $Revision$
     */
    private static final class Constraints {

        private static final GridBagConstraints LBL_STATUS = new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(7, 5, 3, 5), 0, 0);
        private static final GridBagConstraints RB_OPEN = new GridBagConstraints(1, 0, 1, 1, 1, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 3, 0), 0, 0);
        private static final GridBagConstraints RB_SOLVED = new GridBagConstraints(2, 0, 1, 1, 1, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 5), 0, 0);
        private static final GridBagConstraints RB_INVALID = new GridBagConstraints(3, 0, 1, 1, 1, 0,
                GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(2, 3, 3, 5), 0, 0);

        private Constraints() {}
    }
}