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
 *
 * @author Beata
 * @version $Revision$
 */
public class TurnRestriction {

    private final String id;
    private final List<TurnSegment> segments;
    private final LatLon point;
    private final Status status;
    private final TurnType turnType;
    private final TurnConfidenceLevel confidenceLevel;
    private final Integer numberOfPasses;


    public TurnRestriction(final String id, final List<TurnSegment> segments, final LatLon point, final Status status,
            final TurnType turnType, final TurnConfidenceLevel confidenceLevel, final Integer numberOfPasses) {
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

    public TurnType getTurnType() {
        return turnType;
    }

    public TurnConfidenceLevel getConfidenceLevel() {
        return confidenceLevel;
    }

    public Integer getNumberOfPasses() {
        return numberOfPasses;
    }
}