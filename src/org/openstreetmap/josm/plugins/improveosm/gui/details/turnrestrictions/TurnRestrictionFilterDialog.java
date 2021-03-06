/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.turnrestrictions;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openstreetmap.josm.plugins.improveosm.argument.TurnRestrictionFilter;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.BasicFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.BasicFilterPanel;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 * Dialog window that displays the data filters.
 *
 * @author Beata
 * @version $Revision$
 */
public class TurnRestrictionFilterDialog extends BasicFilterDialog {

    private static final long serialVersionUID = -1004737094751848643L;

    /* dialog minimal size */
    private static final Dimension DIM = new Dimension(550, 150);


    public TurnRestrictionFilterDialog() {
        super(DIM);
    }


    @Override
    public BasicFilterPanel createFilterPanel() {
        final TurnRestrictionFilter filter = PreferenceManager.getInstance().loadTurnRestrictionFilter();
        return new FilterPanel(filter);
    }

    @Override
    public AbstractAction getOkAction() {
        return new OkAction();
    }


    /* if the user selects the OK button, then the new search filters are applied */

    private class OkAction extends AbstractAction {

        private static final long serialVersionUID = 2130985524511727521L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            final PreferenceManager prefManager = PreferenceManager.getInstance();
            final TurnRestrictionFilter newFilter = (TurnRestrictionFilter) selectedFilters();
            if (newFilter != null) {
                final TurnRestrictionFilter oldFilter = prefManager.loadTurnRestrictionFilter();
                if (newFilter.equals(oldFilter)) {
                    // no changes
                    prefManager.saveTurnRestrictionFiltersChangedFlag(false);
                } else {
                    prefManager.saveTurnRestrictionFilter(newFilter);
                    prefManager.saveTurnRestrictionFiltersChangedFlag(true);
                }
            }
            dispose();
        }
    }
}