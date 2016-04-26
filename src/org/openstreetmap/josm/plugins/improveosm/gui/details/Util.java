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

import java.util.List;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.AbstractLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.DirectionOfFlowLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.MissingGeometryLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.TurnRestrictionLayer;


/**
 * Holds utility methods used by the GUI classes.
 *
 * @author Beata
 * @version $Revision$
 */
final class Util {

    private Util() {}

    static Layer getImproveOsmLayer() {
        Layer improveOsmLayer = null;
        if (Main.map.mapView.getActiveLayer() instanceof AbstractLayer) {
            improveOsmLayer = Main.map.mapView.getActiveLayer();
        } else {
            // other layer is active
            final List<AbstractLayer> layers = Main.map.mapView.getLayersOfType(AbstractLayer.class);
            for (final AbstractLayer layer : layers) {
                if (hasSelectedItems(layer)) {
                    improveOsmLayer = layer;
                    break;
                }
            }
            if (improveOsmLayer == null) {
                // 1st visible layer
                for (final AbstractLayer layer : layers) {
                    if (layer.isVisible()) {
                        improveOsmLayer = layer;
                        break;
                    }
                }

                // 1st layer that is available
                improveOsmLayer = improveOsmLayer == null ? layers.get(0) : improveOsmLayer;
            }

        }

        return improveOsmLayer;
    }

    private static boolean hasSelectedItems(final AbstractLayer layer) {
        boolean result = false;
        if (layer instanceof MissingGeometryLayer) {
            result = ((MissingGeometryLayer) layer).hasSelectedItems();
        } else if (layer instanceof DirectionOfFlowLayer) {
            result = ((DirectionOfFlowLayer) layer).hasSelectedItems();
        } else if (layer instanceof TurnRestrictionLayer) {
            result = ((TurnRestrictionLayer) layer).hasSelectedItems();
        }
        return result;
    }
}