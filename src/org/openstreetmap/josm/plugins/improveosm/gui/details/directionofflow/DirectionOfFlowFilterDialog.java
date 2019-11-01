/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.directionofflow;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openstreetmap.josm.plugins.improveosm.argument.OnewayFilter;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.BasicFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.BasicFilterPanel;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 * Dialog window that displays the filters that can be applied to the TrafficFlowDirection layer.
 *
 * @author Beata
 * @version $Revision$
 */
public class DirectionOfFlowFilterDialog extends BasicFilterDialog {

    private static final long serialVersionUID = 6838800795969608589L;

    /* dialog minimal size */
    private static final Dimension DIM = new Dimension(550, 150);


    public DirectionOfFlowFilterDialog() {
        super(DIM);
    }


    @Override
    public BasicFilterPanel createFilterPanel() {
        final OnewayFilter filter = PreferenceManager.getInstance().loadOnewayFilter();
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
            final OnewayFilter newFilter = (OnewayFilter) selectedFilters();
            if (newFilter != null) {
                final OnewayFilter oldFilter = prefManager.loadOnewayFilter();
                if (newFilter.equals(oldFilter)) {
                    // no changes
                    prefManager.saveDirectionOfFlowFiltersChangedFlag(false);
                } else {
                    prefManager.saveOnewayFilter(newFilter);
                    prefManager.saveDirectionOfFlowFiltersChangedFlag(true);
                }
            }
            dispose();
        }
    }
}