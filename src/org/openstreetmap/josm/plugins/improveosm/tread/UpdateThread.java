/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.tread;

import javax.swing.SwingUtilities;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MainApplication;
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
        if (MainApplication.getMap() != null && MainApplication.getMap().mapView != null) {
            final Bounds bounds = new Bounds(
                    MainApplication.getMap().mapView.getLatLon(0, MainApplication.getMap().mapView.getHeight()),
                    MainApplication.getMap().mapView.getLatLon(MainApplication.getMap().mapView.getWidth(), 0));

            final BoundingBox bbox = new BoundingBox(bounds.getMax().lat(), bounds.getMin().lat(),
                    bounds.getMax().lon(), bounds.getMin().lon());
            final int zoom = Util.zoom(MainApplication.getMap().mapView.getRealBounds());
            final DataSet<T> result = searchData(bbox, zoom);

            // update UI
            updateUI(result);
        }
    }

    private void updateUI(final DataSet<T> result) {
        SwingUtilities.invokeLater(() -> {
            if (layer != null) {
                layer.setDataSet(result);
                if (result != null && layer.equals(MainApplication.getLayerManager().getActiveLayer())) {
                    final T item = layer.lastSelectedItem();
                    if (item == null) {
                        dialog.updateUI(null, null, null, null, 0);
                    } else if (shouldClearSelection(item)) {
                        layer.updateSelectedItem(null);
                        dialog.updateUI(null, null, null, null, 0);
                    }
                }
                layer.invalidate();
                MainApplication.getMap().mapView.repaint();
            }
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