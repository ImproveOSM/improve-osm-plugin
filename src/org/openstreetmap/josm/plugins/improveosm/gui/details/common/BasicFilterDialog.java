/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.common;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.improveosm.argument.SearchFilter;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.tools.GuiSizesHelper;
import com.telenav.josm.common.gui.CancelAction;
import com.telenav.josm.common.gui.ModalDialog;
import com.telenav.josm.common.gui.builder.ButtonBuilder;
import com.telenav.josm.common.gui.builder.ContainerBuilder;


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


    /**
     * Builds a new filter panel.
     *
     * @param dimension the filter dialog's initial size
     */
    public BasicFilterDialog(final Dimension dimension) {
        super(GuiConfig.getInstance().getDlgFilterTitle(), IconConfig.getInstance().getFilterIcon().getImage(),
                dimension);
        setLocationRelativeTo(MainApplication.getMap().mapView);
        createComponents();
        setSize(GuiSizesHelper.getDimensionDpiAdjusted(dimension));
    }


    @Override
    public void createComponents() {
        pnlFilter = createFilterPanel();
        final JButton btnReset = ButtonBuilder.build(new ResetAction(), GuiConfig.getInstance().getBtnResetLbl());
        final JButton btnOk = ButtonBuilder.build(getOkAction(), GuiConfig.getInstance().getBtnOkLbl());
        final JButton btnCancel =
                ButtonBuilder.build(new CancelAction(this), GuiConfig.getInstance().getBtnCancelLbl());
        final JPanel pnlButton = ContainerBuilder.buildFlowLayoutPanel(FlowLayout.RIGHT, btnReset, btnOk, btnCancel);
        add(pnlFilter, BorderLayout.CENTER);
        add(pnlButton, BorderLayout.SOUTH);
    }

    /**
     * Returns the currently selected search filters.
     *
     * @return a {@code SearchFilter} object
     */
    protected SearchFilter selectedFilters() {
        return pnlFilter.selectedFilters();
    }

    /**
     * Creates a panel containing the filters specific to a data layer.
     *
     * @return a {@code BasicButtonPanel} object
     */
    protected abstract BasicFilterPanel createFilterPanel();

    /**
     * Returns the action corresponding to the "OK" button.
     *
     * @return a {@code AbstractAction}
     */
    protected abstract AbstractAction getOkAction();


    /* if the user selects the reset button, then the search filters are reset to the default one */

    private class ResetAction extends AbstractAction {

        private static final long serialVersionUID = 6640018164566789264L;

        @Override
        public void actionPerformed(final ActionEvent event) {
            pnlFilter.resetFilters();
        }
    }
}