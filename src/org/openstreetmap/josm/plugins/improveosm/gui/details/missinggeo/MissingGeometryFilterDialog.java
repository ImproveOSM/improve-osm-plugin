/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.missinggeo;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.BasicFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.BasicFilterPanel;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 * Dialog window that displays the filters that can be applied to the MissingGeometryLayer.
 *
 * @author Beata
 * @version $Revision$
 */
public class MissingGeometryFilterDialog extends BasicFilterDialog {

    private static final long serialVersionUID = -2173011678464747445L;

    /* dialog minimal size */
    private static final Dimension DIM = new Dimension(400, 230);


    public MissingGeometryFilterDialog() {
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
            final MissingGeometryFilter newFilter = (MissingGeometryFilter) selectedFilters();

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