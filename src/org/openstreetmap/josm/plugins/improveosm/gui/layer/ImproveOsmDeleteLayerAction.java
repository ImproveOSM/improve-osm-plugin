/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright (c)2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.improveosm.gui.layer;

import static org.openstreetmap.josm.tools.I18n.tr;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.openstreetmap.josm.gui.dialogs.LayerListDialog;
import org.openstreetmap.josm.gui.dialogs.layer.DeleteLayerAction;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.tools.ImageProvider;


/**
 *
 * @author beataj
 * @version $Revision$
 */
abstract class ImproveOsmDeleteLayerAction extends AbstractAction {

    private static final long serialVersionUID = 1569467764140753112L;
    private final DeleteLayerAction deleteAction = LayerListDialog.getInstance().createDeleteLayerAction();

    ImproveOsmDeleteLayerAction() {
        super(GuiConfig.getInstance().getDeleteMenuItemLbl());
        new ImageProvider(IconConfig.getInstance().getDeleteIconName()).getResource().attachImageIcon(this, true);
        putValue(SHORT_DESCRIPTION, tr(GuiConfig.getInstance().getDeleteMenuItemTlt()));
        putValue(NAME, tr(GuiConfig.getInstance().getDeleteMenuItemLbl()));
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        saveLayerClosedState();
        deleteAction.actionPerformed(e);
    }

    abstract void saveLayerClosedState();
}