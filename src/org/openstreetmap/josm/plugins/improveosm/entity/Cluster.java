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

import org.openstreetmap.josm.data.coor.LatLon;
import com.telenav.josm.common.entity.EntityUtil;


/**
 * Defines the attributes of the 'Cluster' entity.
 *
 * @author Beata
 * @version $Revision$
 */
public class Cluster implements Comparable<Cluster> {

    private final LatLon point;
    private final Integer size;


    /**
     * Builds a new cluster with the given arguments.
     *
     * @param point the cluster's centroid
     * @param size the number of elements belonging to the cluster
     */
    public Cluster(final LatLon point, final Integer size) {
        this.point = point;
        this.size = size;
    }


    @Override
    public int compareTo(final Cluster object) {
        return size.compareTo(object.getSize());
    }

    public LatLon getPoint() {
        return point;
    }

    public Integer getSize() {
        return size;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + EntityUtil.hashCode(point);
        result = prime * result + EntityUtil.hashCode(size == null);
        return result;
    }


    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj instanceof Cluster) {
            final Cluster other = (Cluster) obj;
            result = EntityUtil.bothNullOrEqual(point, other.getPoint());
            result = result && EntityUtil.bothNullOrEqual(size, other.getSize());
        }
        return result;
    }
}