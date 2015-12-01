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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openstreetmap.josm.plugins.improveosm.argument.OnewayFilter;
import org.openstreetmap.josm.plugins.improveosm.gui.details.CancelAction;
import org.openstreetmap.josm.plugins.improveosm.gui.details.GuiBuilder;
import org.openstreetmap.josm.plugins.improveosm.gui.details.ModalDialog;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public class FilterDialog extends ModalDialog {

    private static final long serialVersionUID = 6838800795969608589L;

    /* dialog minimal size */
    private static final Dimension DIM = new Dimension(450, 150);

    /* UI components */
    private FilterPanel pnlFilter;
    private JPanel pnlButton;

    /**
     * Builds a new filter panel.
     */
    public FilterDialog() {
        super(GuiConfig.getInstance().getDlgFilterTitle(), IconConfig.getInstance().getFilterIcon().getImage());
        createComponents();
        setSize(DIM);
        setMinimumSize(DIM);
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


    /* if the user selects the reset button, then the search filters are reset to the default one */
    private class ResetAction extends AbstractAction {

        private static final long serialVersionUID = 6640018164566789264L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            pnlFilter.resetFilters();
        }
    }


    /* if the user selects the OK button, then the new search filters are applied */
    private class OkAction extends AbstractAction {

        private static final long serialVersionUID = 2130985524511727521L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            final PreferenceManager prefManager = PreferenceManager.getInstance();
            final OnewayFilter newFilter = pnlFilter.selectedFilters();
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