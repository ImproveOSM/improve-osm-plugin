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
package org.openstreetmap.josm.plugins.improveosm.gui.details.missinggeo;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
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

    private static final long serialVersionUID = -2173011678464747445L;

    /* dialog minimal size */
    private static final Dimension DIM = new Dimension(400, 200);


    FilterDialog() {
        super(DIM);
    }


    @Override
    public BasicFilterPanel createFilterPanel() {
        final MissingGeometryFilter filter = PreferenceManager.getInstance().loadMissingGeometryFilter();
        return new FilterPanel(filter);
    }

    @Override
    public AbstractAction getOkAction() {
        return new OkAction();
    }


    /* The action to be executed when the user click's on the 'OK' button */

    private class OkAction extends AbstractAction {

        private static final long serialVersionUID = -2928306625791941775L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            final PreferenceManager prefManager = PreferenceManager.getInstance();
            final MissingGeometryFilter newFilter = (MissingGeometryFilter) getPnlFilter().selectedFilters();

            if (newFilter != null) {
                final MissingGeometryFilter oldFilter = prefManager.loadMissingGeometryFilter();
                if (oldFilter.equals(newFilter)) {
                    prefManager.saveMissingGeometryFiltersChangedFlag(false);
                } else {
                    prefManager.saveMissingGeometryFilter(newFilter);
                    prefManager.saveMissingGeometryFiltersChangedFlag(true);
                }
                dispose();
            }
        }
    }
}