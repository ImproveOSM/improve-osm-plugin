/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.service.directioofflow;

import java.util.List;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.service.entity.BaseResponse;


/**
 * Defines the response of the DirectionOfFlow service.
 *
 * @author Beata
 * @version $Revision$
 */
class Response extends BaseResponse {
    // setters are not required,since GSON sets the fields directly using reflection.

    private List<RoadSegment> roadSegments;

    List<RoadSegment> getRoadSegments() {
        return roadSegments;
    }
}