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
package org.openstreetmap.josm.plugins.improveosm.util.pref;


/**
 * Defines preference entry keys.
 *
 * @author Beata
 * @version $Revision$
 */
public final class Keys {

    private Keys() {}

    /* common keys */
    static final String OSM_USERNAME = "osm-server.username";
    static final String SUPPRESS_ERROR = "improveosm.error.suppress";
    public static final String DATA_LAYER = "improveosm.data.layer";

    /* direction of flow layer related properties */
    static final String DIRECTIONOFFLOW_LAST_COMMENT = "improveosm.directionOfFlow.comment";
    static final String DIRECTIONOFFLOW_TIP_SUPPRESS = "improveosm.directionOfFlow.tip.suppress";
    static final String DIRECTIONOFFLOW_STATUS = "improveosm.directionOfFlow.filter.status";
    static final String DIRECTIONOFFLOW_CONFIDENCE_LEVEL = "improveosm.directionOfFlow.filter.confidence";
    public static final String DIRECTIONOFFLOW_FILTERS_CHANGED = "improveosm.directionOfFlow.filter.changed";

    /* missing geometry layer related properties */
    static final String MISSINGGEO_LAST_COMMENT = "improveosm.missingGeometry.comment";
    static final String MISSINGGEO_STATUS = "improveosm.missingGeometry.filter.status";
    static final String MISSINGGEO_TYPE = "improveosm.missingGeometry.filter.type";
    static final String MISSINGGEO_COUNT = "improveosm.missingGeometry.filter.count";
    static final String MISSINGGEO_INCLUDE_WATER = "improveosm.missingGeometry.filter.includeWater";
    static final String MISSINGGEO_INCLUDE_PATH = "improveosm.missingGeometry.filter.includePath";
    public static final String MISSINGGEO_FILTERS_CHANGED = "improveosm.missingGeometry.filter.changed";

    /* turn restriction layer related properties */
    static final String TURN_RESTRICTION_LAST_COMMENT = "improveosm.turnRestriction.comment";
    static final String TURN_RESTRICTION_STATUS = "improveosm.turnRestriction.filter.status";
    static final String TURN_RESTRICTION_CONFIDENCE_LEVEL = "improveosm.turnRestriction.filter.confidence";
    public static final String TURN_RESTRICTION_FILTERS_CHANGED = "improveosm.turnRestriction.filter.changed";
}