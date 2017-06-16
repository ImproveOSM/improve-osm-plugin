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
import java.util.concurrent.CopyOnWriteArrayList;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.improveosm.entity.DataSet;


/**
 * Defines the common functionality for the improve osm layers.
 *
 * @author Beata
 * @version $Revision$
 * @param <T> the implementation layer object type
 */
public abstract class ImproveOsmLayer<T> extends AbstractLayer {

    private final PaintHandler<T> paintHandler;

    private DataSet<T> dataSet;
    private List<T> selectedItems;


    /**
     * Builds a new layer with the given arguments.
     *
     * @param layerName the name of the layer
     * @param paintHandler the handler responsible of drawing the layer elements
     */
    ImproveOsmLayer(final String layerName, final PaintHandler<T> paintHandler) {
        super(layerName);
        this.paintHandler = paintHandler;
        this.selectedItems = new ArrayList<>();
    }


    @Override
    public void paint(final Graphics2D graphics, final MapView mapView, final Bounds bounds) {
        System.out
                .println(graphics.getClip().getBounds().getWidth() + " " + graphics.getClip().getBounds().getHeight());
        mapView.setDoubleBuffered(true);
        graphics.setRenderingHints(RENDERING_MAP);
        if (dataSet != null) {
            paintHandler.drawDataSet(graphics, mapView, bounds, dataSet, selectedItems);
        }
    }

    public void drawItemsSelector(final Graphics2D graphics, final MapView mapView, final Point oldNorthEast,
            final Point oldNorthWest, final Point northEast, final Point northWest) {
        paintHandler.drawItemsSelector(graphics, mapView, oldNorthEast, oldNorthWest, northEast, northWest);
    }

    /**
     * Sets the layer's data set. Previously selected items will be unselected if the new data set does not contains
     * these elements.
     *
     * @param dataSet a {@code DataSet} containing the items from the current view
     */
    public void setDataSet(final DataSet<T> dataSet) {
        this.dataSet = dataSet;
        if (!selectedItems.isEmpty() || (!dataSet.getClusters().isEmpty())) {
            updateSelectedItems();
        }
    }

    /**
     * Updates the selected items based on the current data set. Previously selected items that are not present in the
     * current data set will be removed.
     */
    void updateSelectedItems() {
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

    /**
     * Updates the selected item.
     *
     * @param item an object representing the currently selected element
     */
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

    /**
     * Returns the item nearby the given point. The method returns null if there is no nearby item.
     *
     * @param point a {@code Point} represents the location where the user clicked
     * @param multiSelect specifies if multiple elements are selected or not
     * @return a {@code T}
     */
    public T nearbyItem(final Point point, final boolean multiSelect) {
        final T item = dataSet != null ? nearbyItem(point) : null;
        if (!multiSelect) {
            selectedItems.clear();
        }
        return item;
    }

    /**
     * Searches for the most close item to the given location. The method returns null if there is no nearby item.
     *
     * @param point a {@code Point} represents the location where the user clicked
     * @return a {@code T}
     */
    abstract T nearbyItem(final Point point);

    /**
     * Returns the last selected item. If no item is selected the method return null.
     *
     * @return a {@code T}
     */
    public T lastSelectedItem() {
        return selectedItems.isEmpty() ? null : selectedItems.get(selectedItems.size() - 1);
    }

    public DataSet<T> getDataSet() {
        return dataSet;
    }

    public boolean hasSelectedItems() {
        return selectedItems != null && !selectedItems.isEmpty();
    }

    public List<T> getSelectedItems() {
        return new CopyOnWriteArrayList<>(selectedItems);
    }

    void setSelectedItems(final List<T> selectedItems) {
        this.selectedItems = selectedItems;
    }
}