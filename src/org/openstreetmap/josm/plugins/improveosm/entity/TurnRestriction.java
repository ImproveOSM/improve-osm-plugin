/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.entity;

import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;
import com.telenav.josm.common.entity.EntityUtil;


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
        result = id != null ? prime * result + id.hashCode() : prime * result + EntityUtil.hashCode(point);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            final TurnRestriction other = (TurnRestriction) obj;
            if (id != null && other.getId() != null) {
                result = id.equals(other.getId());
            } else {
                result = EntityUtil.bothNullOrEqual(point, other.getPoint());
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return id != null ? id : (turnRestrictions != null ? turnRestrictions.toString() : "");
    }
}