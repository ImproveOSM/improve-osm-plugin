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
package org.openstreetmap.josm.plugins.improveosm.tread;

import javax.swing.SwingUtilities;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.plugins.improveosm.entity.DataSet;
import org.openstreetmap.josm.plugins.improveosm.gui.details.ImproveOsmDetailsDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.ImproveOsmLayer;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import com.telenav.josm.common.argument.BoundingBox;


/**
 * Downloads the data from the current bounding box using the current filters and updates the UI accordingly. This class
 * defines the common functionality of the data update process.
 *
 * @author Beata
 * @version $Revision$
 * @param <T>
 */
public abstract class UpdateThread<T> implements Runnable {

    private final ImproveOsmDetailsDialog dialog;
    private final ImproveOsmLayer<T> layer;


    UpdateThread(final ImproveOsmDetailsDialog dialog, final ImproveOsmLayer<T> layer) {
        this.dialog = dialog;
        this.layer = layer;
    }

    @Override
    public void run() {
        if (Main.map != null && Main.map.mapView != null) {
            final Bounds bounds = new Bounds(Main.map.mapView.getLatLon(0, Main.map.mapView.getHeight()),
                    Main.map.mapView.getLatLon(Main.map.mapView.getWidth(), 0));

            final BoundingBox bbox = new BoundingBox(bounds.getMax().lat(), bounds.getMin().lat(),
                    bounds.getMax().lon(), bounds.getMin().lon());
            final int zoom = Util.zoom(Main.map.mapView.getRealBounds());
            final DataSet<T> result = searchData(bbox, zoom);

            // update UI
            updateUI(result);
        }
    }

    private void updateUI(final DataSet<T> result) {
        SwingUtilities.invokeLater(() -> {
            layer.setDataSet(result);
            if (result != null && Main.getLayerManager().getActiveLayer().equals(layer)) {
                final T item = layer.lastSelectedItem();
                if (item == null) {
                    dialog.updateUI(null, null, null, null, 0);
                } else if (shouldClearSelection(item)) {
                    layer.updateSelectedItem(null);
                    dialog.updateUI(null, null, null, null, 0);
                }
            }
            layer.invalidate();
            Main.map.mapView.repaint();
        });
    }

    abstract boolean shouldClearSelection(T item);

    /**
     * Searches for data in the given bounding box and zoom level.
     *
     * @param bbox the current searching area
     * @param zoom the current zoom level
     * @return a {@code DataSet}
     */
    abstract DataSet<T> searchData(BoundingBox bbox, int zoom);
}