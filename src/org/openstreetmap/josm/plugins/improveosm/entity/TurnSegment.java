/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
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