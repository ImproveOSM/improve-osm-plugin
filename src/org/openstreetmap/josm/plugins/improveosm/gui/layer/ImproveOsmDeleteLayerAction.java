/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
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