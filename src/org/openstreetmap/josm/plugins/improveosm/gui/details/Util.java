/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details;

import java.util.List;
import org.openstreetmap.josm.gui.MainApplication;
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
        if (MainApplication.getLayerManager().getActiveLayer() instanceof AbstractLayer) {
            improveOsmLayer = MainApplication.getLayerManager().getActiveLayer();
        } else {
            // other layer is active
            final List<AbstractLayer> layers = MainApplication.getLayerManager().getLayersOfType(AbstractLayer.class);
            for (final AbstractLayer layer : layers) {
                if (hasSelectedItems(layer)) {
                    improveOsmLayer = layer;
                    break;
                }
            }
            if (improveOsmLayer == null && !layers.isEmpty()) {
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