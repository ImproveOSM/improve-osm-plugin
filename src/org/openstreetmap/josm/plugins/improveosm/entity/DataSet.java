/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Represent's the data set of the plugin layers.
 *
 * @author Beata
 * @version $Revision$
 * @param <T> the object type
 */
public class DataSet<T> {

    private final List<Cluster> clusters;
    private final List<T> items;


    /**
     * Builds an empty data set.
     */
    public DataSet() {
        clusters = new ArrayList<>();
        items = new ArrayList<>();
    }

    /**
     * Builds a data set based on the given arguments.
     *
     * @param clusters a list of {@code Cluster}s. If not null, the list is ordered by size ascending.
     * @param items a list if {@code T} items
     */
    public DataSet(final List<Cluster> clusters, final List<T> items) {
        this.clusters = clusters == null ? new ArrayList<>() : clusters;
        if (!this.clusters.isEmpty()) {
            Collections.sort(this.clusters);
        }
        this.items = items == null ? new ArrayList<>() : items;
    }


    public List<Cluster> getClusters() {
        return clusters;
    }

    public List<T> getItems() {
        return items;
    }
}