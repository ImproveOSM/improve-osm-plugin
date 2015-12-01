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

import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.CLUSTER_COMPOSITE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.CLUSTER_DEF_RADIUS;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.NORMAL_COMPOSITE;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.improveosm.entity.Cluster;
import org.openstreetmap.josm.plugins.improveosm.entity.DataSet;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.ClusterConfig;


/**
 * Defines a general paint handler.
 *
 * @author Beata
 * @version $Revision$
 */
abstract class PaintHandler<T> {

    abstract void drawDataSet(final Graphics2D graphics, final MapView mapView, final Bounds bounds,
            final DataSet<T> dataSet, final List<T> items);

    void drawClusters(final Graphics2D graphics, final MapView mapView, final List<Cluster> clusters, final int zoom,
            final Color color) {
        final SortedMap<Integer, Double> clusterRadiusMap = generateClusterRadiusMap(zoom, clusters);
        graphics.setComposite(CLUSTER_COMPOSITE);
        for (final Cluster cluster : clusters) {
            final Double radius = clusterRadius(clusterRadiusMap, cluster.getSize());
            PaintUtil.drawCircle(graphics, mapView.getPoint2D(cluster.getPoint()), color, radius);
        }
        graphics.setComposite(NORMAL_COMPOSITE);
    }

    private SortedMap<Integer, Double> generateClusterRadiusMap(final int zoom, final List<Cluster> clusters) {
        SortedMap<Integer, Double> map;
        final int max = clusters.get(clusters.size() - 1).getSize();
        final List<Double> radiusList = ClusterConfig.getInstance().getRadiusList(zoom);
        final int count = ClusterConfig.getInstance().getCount();
        if (clusters.size() > 1) {
            final int x = max / count;
            map = new TreeMap<>();
            int i;
            for (i = 1; i <= count - 1; i++) {
                map.put(i * x, radiusList.get(i - 1));
            }
            map.put(max, radiusList.get(i - 1));
        } else {
            map = new TreeMap<>();
            map.put(max, radiusList.get(count - 1));
        }
        return map;
    }

    private Double clusterRadius(final SortedMap<Integer, Double> map, final Integer value) {
        Double radius = null;
        if (map.size() > 1) {
            for (final Integer key : map.keySet()) {
                if (value <= key) {
                    radius = map.get(key);
                    break;
                }
            }
        } else {
            radius = map.get(value);
        }
        return radius != null ? radius : CLUSTER_DEF_RADIUS;
    }
}