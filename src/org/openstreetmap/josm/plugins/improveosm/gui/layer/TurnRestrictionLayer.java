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
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;
import javax.swing.Icon;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.improveosm.entity.DataSet;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.TurnRestrictionGuiConfig;

/**
 *
 * @author Beata
 * @version $Revision$
 */
public class TurnRestrictionLayer extends ImproveOsmLayer<TurnRestriction> {

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


    private static class TurnRestrictionPaintHandler extends PaintHandler<TurnRestriction> {

        @Override
        void drawDataSet(final Graphics2D graphics, final MapView mapView, final Bounds bounds,
                final DataSet<TurnRestriction> dataSet, final List<TurnRestriction> items) {
            final int zoom = Util.zoom(bounds);
            if (zoom > Config.getTurnRestrictionInstance().getMaxClusterZoom()) {
                // display segments
                if (dataSet.getItems() != null && !dataSet.getItems().isEmpty()) {
                    for (final TurnRestriction turnRestriction : dataSet.getItems()) {
                        if (!items.contains(turnRestriction)) {
                            PaintUtil.drawTunRestriction(graphics, mapView, turnRestriction, false);
                        }
                    }
                    for (final TurnRestriction turnRestriction : items) {
                        PaintUtil.drawTunRestriction(graphics, mapView, turnRestriction, true);
                    }
                }
            } else {
                // display clusters
                if (dataSet.getClusters() != null && !dataSet.getClusters().isEmpty()) {
                    drawClusters(graphics, mapView, dataSet.getClusters(), zoom, TURNRESTRICTION_CLUSTER_COLOR);
                }
            }
        }

    }
}