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

import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TURNRESTRICTION_CLUSTER_COLOR;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.TurnRestrictionGuiConfig;


/**
 * Defines the TurnRestrictionLayer main functionality.
 *
 * @author Beata
 * @version $Revision$
 */
public class TurnRestrictionLayer extends ImproveOsmLayer<TurnRestriction> {

    /**
     * Builds a new TurnRestriction layer.
     */
    public TurnRestrictionLayer() {
        super(TurnRestrictionGuiConfig.getInstance().getLayerName(), new TurnRestrictionPaintHandler());
    }

    @Override
    public Icon getIcon() {
        return IconConfig.getInstance().getTurnRestrictonLayerIcon();
    }

    @Override
    public String getToolTipText() {
        return TurnRestrictionGuiConfig.getInstance().getLayerTlt();
    }

    @Override
    TurnRestriction nearbyItem(final Point point) {
        return Util.nearbyTurnRestriction(getDataSet().getItems(), point);
    }

    @Override
    void updateSelectedItems() {
        final List<TurnRestriction> newList = new ArrayList<>();
        for (final TurnRestriction item : this.getSelectedItems()) {
            if (getDataSet().getItems().contains(item)) {
                if (item.getTurnRestrictions() == null) {
                    newList.add(item);
                } else {
                    final int idx = getDataSet().getItems().indexOf(item);
                    final TurnRestriction newItem = getDataSet().getItems().get(idx);
                    newList.add(newItem);
                }
            }
            setSelectedItems(newList);
        }
    }

    /*
     * Draws the layer's data items to the map.
     */
    private static class TurnRestrictionPaintHandler extends PaintHandler<TurnRestriction> {

        @Override
        boolean displayClusters(final int zoom) {
            return zoom <= Config.getTurnRestrictionInstance().getMaxClusterZoom();
        }

        @Override
        void drawItem(final Graphics2D graphics, final MapView mapView, final TurnRestriction item,
                final boolean selected) {
            PaintUtil.drawTunRestriction(graphics, mapView, item, selected);
        }

        @Override
        Color getClusterColor() {
            return TURNRESTRICTION_CLUSTER_COLOR;
        }
    }
}