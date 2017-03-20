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
package org.openstreetmap.josm.plugins.improveosm.gui.layer;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.openstreetmap.josm.data.osm.visitor.BoundingXYVisitor;
import org.openstreetmap.josm.gui.dialogs.LayerListDialog;
import org.openstreetmap.josm.gui.dialogs.LayerListPopup;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.BasicFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.GuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;


/**
 * Defines the common JOSM Layer relayed functionality.
 *
 * @author Beata
 * @version $Revision$
 */
public abstract class AbstractLayer extends Layer {

    private static final String DELETE_ACTION = "delete";

    public AbstractLayer(final String layerName) {
        super(layerName);

        LayerListDialog.getInstance().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), DELETE_ACTION);
        LayerListDialog.getInstance().getActionMap().put(DELETE_ACTION, getDeleteAction());
    }


    @Override
    public Object getInfoComponent() {
        return getToolTipText();
    }

    @Override
    public Action[] getMenuEntries() {
        final LayerListDialog layerListDialog = LayerListDialog.getInstance();
        return new Action[] { layerListDialog.createActivateLayerAction(this),
                layerListDialog.createShowHideLayerAction(), getDeleteAction(), SeparatorLayerAction.INSTANCE,
                getFilterAction(), SeparatorLayerAction.INSTANCE, new LayerListPopup.InfoAction(this) };
    }

    private Action getFilterAction() {
        return new AbstractAction(GuiConfig.getInstance().getDlgFilterTitle(),
                IconConfig.getInstance().getFilterIcon()) {

            private static final long serialVersionUID = -3017461809233825500L;

            @Override
            public void actionPerformed(final ActionEvent event) {
                getFilterDialog().setVisible(true);
            }
        };
    }

    abstract AbstractAction getDeleteAction();

    abstract BasicFilterDialog getFilterDialog();

    @Override
    public boolean isMergable(final Layer layer) {
        return false;
    }

    @Override
    public void mergeFrom(final Layer layer) {
        // this operation is not supported
    }

    @Override
    public void visitBoundingBox(final BoundingXYVisitor visitor) {
        // this operation is not supported
    }
}