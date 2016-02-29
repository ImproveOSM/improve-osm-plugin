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

import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.MISSINGGEO_CLUSTER_COLOR;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.swing.Icon;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.BasicFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.missinggeo.MissingGeometryFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.MissingGeometryGuiConfig;


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
        super(MissingGeometryGuiConfig.getInstance().getLayerName(), new MissingGeometryHanlder());
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

    /*
     * Draws the layer's data items to the map.
     */
    private static class MissingGeometryHanlder extends PaintHandler<Tile> {

        @Override
        boolean displayClusters(final int zoom) {
            return zoom <= Config.getMissingGeometryInstance().getMaxClusterZoom();
        }

        @Override
        void drawItem(final Graphics2D graphics, final MapView mapView, final Tile item,
                final boolean selected) {
            PaintUtil.drawTile(graphics, mapView, item, selected);
        }

        @Override
        Color getClusterColor() {
            return MISSINGGEO_CLUSTER_COLOR;
        }
    }
}