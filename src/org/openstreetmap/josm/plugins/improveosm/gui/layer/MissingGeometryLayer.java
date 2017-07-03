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

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.BasicFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.missinggeo.MissingGeometryFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.MissingGeometryGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 * Defines the MissingGeometryLayer main functionality.
 *
 * @author Beata
 * @version $Revision$
 */
public class MissingGeometryLayer extends ImproveOsmLayer<Tile> {

    /**
     * Builds a new MissingGeometry layer.
     */
    public MissingGeometryLayer() {
        super(MissingGeometryGuiConfig.getInstance().getLayerName(), new MissingGeometryPaintHandler());
    }


    @Override
    public Icon getIcon() {
        return IconConfig.getInstance().getMissingGeometryLayerIcon();
    }

    @Override
    public String getToolTipText() {
        return MissingGeometryGuiConfig.getInstance().getLayerTlt();
    }

    @Override
    Tile nearbyItem(final Point point) {
        return Util.nearbyTile(getDataSet().getItems(), point);
    }

    @Override
    BasicFilterDialog getFilterDialog() {
        return new MissingGeometryFilterDialog();
    }

    @Override
    AbstractAction getDeleteAction() {
        return new DeleteMissingGeometryLayerAction();
    }

    /**
     * Updates the layer selected items with the items found in the given bounding box.
     *
     * @param boundingBox the area in which the items need to be located in order to be selected
     * @param multiSelected specify if the new selected items need to replace the old selected items or not
     */
    public void updateSelectedItems(final Rectangle2D boundingBox, final boolean multiSelected) {
        final List<Tile> selectedItems = getSelectedItems();
        if (!multiSelected) {
            setSelectedItems(getItemsInsideTheBoundingBox(boundingBox));
        } else {
            selectedItems.addAll(getItemsInsideTheBoundingBox(boundingBox).stream()
                    .filter(item -> !getSelectedItems().contains(item))
                    .collect(Collectors.toList()));
            setSelectedItems(selectedItems);
        }
    }

    private List<Tile> getItemsInsideTheBoundingBox(final Rectangle2D boundingBox) {
        return getDataSet().getItems().stream()
                .filter(tile -> Util.tileIntersectsBoundingBox(tile, boundingBox))
                .collect(Collectors.toList());
    }


    private static class DeleteMissingGeometryLayerAction extends ImproveOsmDeleteLayerAction {

        private static final long serialVersionUID = -6587863325888182227L;

        @Override
        void saveLayerClosedState() {
            PreferenceManager.getInstance().saveMissingGeometryLayerOpenedFlag(false);
        }
    }
}