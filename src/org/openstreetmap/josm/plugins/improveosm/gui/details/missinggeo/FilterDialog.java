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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.gui.details.CancelAction;
import org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder;
import org.openstreetmap.josm.plugins.improveosm.gui.details.ModalDialog;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 * Dialog window that displays the data filters.
 *
 * @author Beata
 * @version $Revision$
 */
class FilterDialog extends ModalDialog {

    private static final long serialVersionUID = -2173011678464747445L;

    /* dialog minimal size */
    private static final Dimension DIM = new Dimension(400, 200);

    /* UI components */
    private FilterPanel pnlFilter;
    private JPanel pnlButton;


    /**
     * Builds a new filter dialog window
     */
    public FilterDialog() {
        super(GuiConfig.getInstance().getDlgFilterTitle(), IconConfig.getInstance().getFilterIcon().getImage());
        setSize(DIM);
        setMinimumSize(DIM);
        createComponents();
    }


    @Override
    protected void createComponents() {
        pnlFilter = new FilterPanel();
        final JButton btnReset = GuiBuilder.buildButton(new ResetAction(), GuiConfig.getInstance().getBtnResetLbl());
        final JButton btnOk = GuiBuilder.buildButton(new OkAction(), GuiConfig.getInstance().getBtnOkLbl());
        final JButton btnCancel =
                GuiBuilder.buildButton(new CancelAction(this), GuiConfig.getInstance().getBtnCancelLbl());
        pnlButton = GuiBuilder.buildFlowLayoutPanel(FlowLayout.RIGHT, btnReset, btnOk, btnCancel);
        add(pnlFilter, BorderLayout.CENTER);
        add(pnlButton, BorderLayout.SOUTH);
    }


    /* The action to be executed when the user click's on the 'OK' button */

    private class OkAction extends AbstractAction {

        private static final long serialVersionUID = -2928306625791941775L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            final PreferenceManager prefManager = PreferenceManager.getInstance();
            final MissingGeometryFilter newFilter = pnlFilter.selectedFilters();

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

    /* The action to be executed when the user click's on the 'Reset' button */

    private class ResetAction extends AbstractAction {

        private static final long serialVersionUID = 4708883950358902356L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            pnlFilter.resetFilters();
        }
    }
}