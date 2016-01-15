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
package org.openstreetmap.josm.plugins.improveosm.gui.details;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;


/**
 * Defines the common functionality of the filter dialogs used by the data layers.
 *
 * @author Beata
 * @version $Revision$
 */
public abstract class BasicFilterDialog extends ModalDialog {

    private static final long serialVersionUID = -9194599030250616025L;

    /* UI components */
    private BasicFilterPanel pnlFilter;
    private JPanel pnlButton;


    /**
     * Builds a new filter panel.
     */
    public BasicFilterDialog(final Dimension dimension) {
        super(GuiConfig.getInstance().getDlgFilterTitle(), IconConfig.getInstance().getFilterIcon().getImage());
        createComponents();
        setSize(dimension);
        setMinimumSize(dimension);
    }


    @Override
    protected void createComponents() {
        pnlFilter = createFilterPanel();
        final JButton btnReset = GuiBuilder.buildButton(new ResetAction(), GuiConfig.getInstance().getBtnResetLbl());
        final JButton btnOk = GuiBuilder.buildButton(getOkAction(), GuiConfig.getInstance().getBtnOkLbl());
        final JButton btnCancel =
                GuiBuilder.buildButton(new CancelAction(this), GuiConfig.getInstance().getBtnCancelLbl());
        pnlButton = GuiBuilder.buildFlowLayoutPanel(FlowLayout.RIGHT, btnReset, btnOk, btnCancel);
        add(pnlFilter, BorderLayout.CENTER);
        add(pnlButton, BorderLayout.SOUTH);
    }

    public BasicFilterPanel getPnlFilter() {
        return pnlFilter;
    }

    /**
     * Creates a panel containing the filters specific to a data layer.
     *
     * @return a {@code BasicButtonPanel} object
     */
    public abstract BasicFilterPanel createFilterPanel();

    /**
     * Returns the action corresponding to the "OK" button.
     *
     * @return a {@code AbstractAction}
     */
    public abstract AbstractAction getOkAction();

    /* if the user selects the reset button, then the search filters are reset to the default one */

    private class ResetAction extends AbstractAction {

        private static final long serialVersionUID = 6640018164566789264L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            pnlFilter.resetFilters();
        }
    }
}