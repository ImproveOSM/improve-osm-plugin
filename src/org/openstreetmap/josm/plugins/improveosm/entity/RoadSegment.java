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
import com.telenav.josm.common.entity.EntityUtil;


/**
 * Defines the attributes of the road segment business entity.
 *
 * @author Beata
 * @version $Revision$
 */
public class RoadSegment {

    private final Long wayId;
    private final Long fromNodeId;
    private final Long toNodeId;
    private Integer numberOfTrips;
    private Double percentOfTrips;
    private OnewayConfidenceLevel confidenceLevel;
    private List<LatLon> points;
    private Status status;
    private String type;


    /**
     * Builds a new road segment with the given arguments.
     *
     * @param wayId the identifier of the OSM way which contains this segment
     * @param fromNodeId the identifier of the OSM node which marks the start of this road segment
     * @param toNodeId the identifier of the OSM node which marks the end of this road segment
     */
    public RoadSegment(final Long wayId, final Long fromNodeId, final Long toNodeId) {
        this.wayId = wayId;
        this.fromNodeId = fromNodeId;
        this.toNodeId = toNodeId;
    }

    /**
     * Builds a new road segment with the given arguments.
     *
     * @param wayId the identifier of the OSM way which contains this segment
     * @param fromNodeId the identifier of the OSM node which marks the start of this road segment
     * @param toNodeId the identifier of the OSM node which marks the end of this road segment
     * @param numberOfTrips the number of trips that passed through the road segment
     * @param percentOfTrips the percent of trips that passed through the road segment
     * @param confidenceLevel the measure of confidence with which this road segment is marked as one way
     * @param points the geometry of the segment, ordered in the direction of the one way restriction
     * @param status the status of the road segment
     * @param type the type of the OSM way which contains this road segment
     */
    public RoadSegment(final Long wayId, final Long fromNodeId, final Long toNodeId, final Integer numberOfTrips,
            final Double percentOfTrips, final OnewayConfidenceLevel confidenceLevel, final List<LatLon> points,
            final Status status, final String type) {
        this(wayId, fromNodeId, toNodeId);
        this.numberOfTrips = numberOfTrips;
        this.percentOfTrips = percentOfTrips;
        this.confidenceLevel = confidenceLevel;
        this.points = points;
        this.status = status;
        this.type = type;
    }


    public Long getWayId() {
        return wayId;
    }

    public Long getFromNodeId() {
        return fromNodeId;
    }

    public Long getToNodeId() {
        return toNodeId;
    }

    public Integer getNumberOfTrips() {
        return numberOfTrips;
    }

    public Double getPercentOfTrips() {
        return percentOfTrips;
    }

    public OnewayConfidenceLevel getConfidenceLevel() {
        return confidenceLevel;
    }

    public List<LatLon> getPoints() {
        return points;
    }

    public Status getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + EntityUtil.hashCode(wayId);
        result = prime * result + EntityUtil.hashCode(fromNodeId);
        result = prime * result + EntityUtil.hashCode(toNodeId);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            final RoadSegment other = (RoadSegment) obj;
            result = EntityUtil.bothNullOrEqual(wayId, other.getWayId());
            result = result && EntityUtil.bothNullOrEqual(fromNodeId, other.getFromNodeId());
            result = result && EntityUtil.bothNullOrEqual(toNodeId, other.getToNodeId());
        }
        return result;
    }

    @Override
    public String toString() {
        return wayId + "-" + fromNodeId + "-" + toNodeId;
    }
}