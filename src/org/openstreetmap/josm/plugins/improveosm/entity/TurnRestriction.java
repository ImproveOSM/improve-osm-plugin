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
 * Defines the turn restriction business entity.
 *
 * @author Beata
 * @version $Revision$
 */
public class TurnRestriction {

    private String id;
    private List<TurnSegment> segments;
    private final LatLon point;
    private Status status;
    private String turnType;
    private TurnConfidenceLevel confidenceLevel;
    private Integer numberOfPasses;

    /*
     * this field is used together with point for situations where there are several turn restrictions located in the
     * same point
     */
    private List<TurnRestriction> turnRestrictions;

    /**
     * Builds a complex turn restriction.
     *
     * @param point a {@code LatLon} represents the turn restrictions location
     * @param turnRestrictions the list of {@code TurnRestriction} available
     */
    public TurnRestriction(final LatLon point, final List<TurnRestriction> turnRestrictions) {
        this.point = point;
        this.turnRestrictions = turnRestrictions;
    }

    /**
     * Builds a simple turn restriction.
     *
     * @param id the unique identifier
     * @param segments represents the segments that leads to the turn restriction and the segments that follows after
     * the turn restriction
     * @param point a {@code LatLon} represents the turn restriction location
     * @param status indicates the turn restriction state
     * @param turnType the turn restriction type
     * @param confidenceLevel the turn restriction confidence level
     * @param numberOfPasses the number of trips that passed the turn restriction
     */
    public TurnRestriction(final String id, final List<TurnSegment> segments, final LatLon point, final Status status,
            final String turnType, final TurnConfidenceLevel confidenceLevel, final Integer numberOfPasses) {
        this.id = id;
        this.segments = segments;
        this.point = point;
        this.status = status;
        this.turnType = turnType;
        this.confidenceLevel = confidenceLevel;
        this.numberOfPasses = numberOfPasses;
    }


    public String getId() {
        return id;
    }

    public List<TurnSegment> getSegments() {
        return segments;
    }

    public LatLon getPoint() {
        return point;
    }

    public Status getStatus() {
        return status;
    }

    public String getTurnType() {
        return turnType;
    }

    public TurnConfidenceLevel getConfidenceLevel() {
        return confidenceLevel;
    }

    public Integer getNumberOfPasses() {
        return numberOfPasses;
    }

    public List<TurnRestriction> getTurnRestrictions() {
        return turnRestrictions;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        if (id != null) {
            result = prime * result + id.hashCode();
        } else {
            result = prime * result + EntityUtil.hashCode(point);
        }
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj instanceof TurnRestriction) {
            final TurnRestriction other = (TurnRestriction) obj;
            if (id != null && other.getId() != null) {
                result = id.equals(other.getId());
            } else {
                result = EntityUtil.bothNullOrEqual(point, other.getPoint());
            }
        }
        return result;
    }
}