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
    static final String LOCATION_PREF_OPTION = "improveosm.locationPref.option";
    static final String LOCATION_PREF_VALUE = "improveosm.locationPref.value";
    static final String LOCATION_TIP_SUPPRESS = "improveosm.location.tip.suppress";

    /* direction of flow layer related properties */
    static final String DOF_LAST_COMMENT = "improveosm.directionOfFlow.comment";
    static final String DOF_TIP_SUPPRESS = "improveosm.directionOfFlow.tip.suppress";
    static final String DOF_STATUS = "improveosm.directionOfFlow.filter.status";
    static final String DOF_CONFIDENCE_LEVEL = "improveosm.directionOfFlow.filter.confidence";
    public static final String DOF_FILTERS_CHANGED = "improveosm.directionOfFlow.filter.changed";

    /* missing geometry layer related properties */
    static final String MG_LAST_COMMENT = "improveosm.missingGeometry.comment";
    static final String MG_STATUS = "improveosm.missingGeometry.filter.status";
    static final String MG_TYPE = "improveosm.missingGeometry.filter.type";
    static final String MG_POINT_COUNT = "improveosm.missingGeometry.filter.pointCount";
    static final String MG_TRIP_COUNT = "improveosm.missingGeometry.filter.tripCount";
    static final String MG_INCLUDE_WATER = "improveosm.missingGeometry.filter.includeWater";
    static final String MG_INCLUDE_PATH = "improveosm.missingGeometry.filter.includePath";
    public static final String MG_FILTERS_CHANGED = "improveosm.missingGeometry.filter.changed";

    /* turn restriction layer related properties */
    static final String TR_LAST_COMMENT = "improveosm.turnRestriction.comment";
    static final String TR_STATUS = "improveosm.turnRestriction.filter.status";
    static final String TR_CONFIDENCE_LEVEL = "improveosm.turnRestriction.filter.confidence";
    public static final String TR_FILTERS_CHANGED = "improveosm.turnRestriction.filter.changed";

    static final String OLD_PLUGINS_WARNING_SUPPRESS = "improveosm.oldPlugins.warning.suppress";
    static final String PLUGINS = "plugins";

    static final String MG_LAYER_OPENED = "improveosm.missingGeometry.layerOpened";
    static final String DOF_LAYER_OPENED = "improveosm.directionOfFlow.layerOpened";
    static final String TR_LAYER_OPENED = "improveosm.turnRestriction.layerOpened";
    static final String PANEL_OPENED = "improveosm.panelOpened";
    public static final String PANEL_ICON_VISIBILITY = "improveosm_logo_25x25.png.visible";

}