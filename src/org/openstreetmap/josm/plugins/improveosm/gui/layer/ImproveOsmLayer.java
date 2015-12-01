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

import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.RENDERING_MAP;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.improveosm.entity.DataSet;


/**
 * Defines the common functionality for the improve osm layers.
 *
 * @author Beata
 * @version $Revision$
 * @param <T>
 */
abstract class ImproveOsmLayer<T> extends AbstractLayer {

    private final PaintHandler<T> paintHandler;

    private DataSet<T> dataSet;
    private List<T> selectedItems;


    ImproveOsmLayer(final String layerName, final PaintHandler<T> paintHandler) {
        super(layerName);
        this.paintHandler = paintHandler;
        this.selectedItems = new ArrayList<>();
    }


    @Override
    public void paint(final Graphics2D graphics, final MapView mapView, final Bounds bounds) {
        mapView.setDoubleBuffered(true);
        graphics.setRenderingHints(RENDERING_MAP);
        if (dataSet != null) {
            paintHandler.drawDataSet(graphics, mapView, bounds, dataSet, selectedItems);
        }
    }

    public void setDataSet(final DataSet<T> dataSet) {
        this.dataSet = dataSet;
        if (!selectedItems.isEmpty() && !this.dataSet.getItems().isEmpty()) {
            final List<T> newList = new ArrayList<>();
            for (final T item : this.selectedItems) {
                if (this.dataSet.getItems().contains(item)) {
                    final int idx = this.dataSet.getItems().indexOf(item);
                    final T newItem = this.dataSet.getItems().get(idx);
                    newList.add(newItem);
                }
            }
            this.selectedItems = newList;
        }
    }

    public void updateSelectedItem(final T item) {
        if (item == null) {
            selectedItems.clear();
        } else {
            final int idx = selectedItems.indexOf(item);
            if (idx > -1) {
                selectedItems.remove(item);
                selectedItems.add(idx, item);
            } else {
                selectedItems.add(item);
            }
        }
    }

    public T nearbyItem(final Point point, final boolean multiSelect) {
        final T item = dataSet != null ? nearbyItem(point) : null;
        if (!multiSelect) {
            selectedItems.clear();
        }
        return item;
    }

    abstract T nearbyItem(final Point point);

    public T lastSelectedItem() {
        return selectedItems.isEmpty() ? null : selectedItems.get(selectedItems.size() - 1);
    }

    public DataSet<T> getDataSet() {
        return dataSet;
    }

    public List<T> getSelectedItems() {
        return selectedItems;
    }
}