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

import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;


/**
 * Defines the attributes of a turn segments.
 *
 * @author Beata
 * @version $Revision$
 */
public class TurnSegment {

    private final List<LatLon> points;
    private final Integer numberOfTrips;


    /**
     * Builds a turn segment based on the given arguments.
     *
     * @param points represent the geometry of the segment
     * @param numberOfTrips represents the number of trips that passed the segment
     */
    public TurnSegment(final List<LatLon> points, final Integer numberOfTrips) {
        this.points = points;
        this.numberOfTrips = numberOfTrips;
    }


    public List<LatLon> getPoints() {
        return points;
    }

    public Integer getNumberOfTrips() {
        return numberOfTrips;
    }
}