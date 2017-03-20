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