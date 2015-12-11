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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openstreetmap.josm.plugins.improveosm.argument.OnewayFilter;
import org.openstreetmap.josm.plugins.improveosm.gui.details.BasicFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.BasicFilterPanel;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 * Dialog window that displays the data filters.
 *
 * @author Beata
 * @version $Revision$
 */
class FilterDialog extends BasicFilterDialog {

    private static final long serialVersionUID = 6838800795969608589L;

    /* dialog minimal size */
    private static final Dimension DIM = new Dimension(450, 150);


    /**
     * Builds a new filter panel.
     */
    FilterDialog() {
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
            final OnewayFilter newFilter = (OnewayFilter) getPnlFilter().selectedFilters();
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