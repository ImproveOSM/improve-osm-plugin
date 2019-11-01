/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.util.pref;


/**
 * Defines preference entry keys.
 *
 * @author Beata
 * @version $Revision$
 */
final class Keys {

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
    static final String DOF_FILTERS_CHANGED = "improveosm.directionOfFlow.filter.changed";

    /* missing geometry layer related properties */
    static final String MG_LAST_COMMENT = "improveosm.missingGeometry.comment";
    static final String MG_STATUS = "improveosm.missingGeometry.filter.status";
    static final String MG_TYPE = "improveosm.missingGeometry.filter.type";
    static final String MG_POINT_COUNT = "improveosm.missingGeometry.filter.pointCount";
    static final String MG_TRIP_COUNT = "improveosm.missingGeometry.filter.tripCount";
    static final String MG_INCLUDE_WATER = "improveosm.missingGeometry.filter.includeWater";
    static final String MG_INCLUDE_PATH = "improveosm.missingGeometry.filter.includePath";
    static final String MG_FILTERS_CHANGED = "improveosm.missingGeometry.filter.changed";

    /* turn restriction layer related properties */
    static final String TR_LAST_COMMENT = "improveosm.turnRestriction.comment";
    static final String TR_STATUS = "improveosm.turnRestriction.filter.status";
    static final String TR_CONFIDENCE_LEVEL = "improveosm.turnRestriction.filter.confidence";
    static final String TR_FILTERS_CHANGED = "improveosm.turnRestriction.filter.changed";

    static final String PLUGINS = "plugins";

    static final String MG_LAYER_OPENED = "improveosm.missingGeometry.layerOpened";
    static final String DOF_LAYER_OPENED = "improveosm.directionOfFlow.layerOpened";
    static final String TR_LAYER_OPENED = "improveosm.turnRestriction.layerOpened";
    static final String PANEL_OPENED = "improveosm.panelOpened";
    static final String PANEL_ICON_VISIBILITY = "improveosm_logo_25x25.png.visible";

    private Keys() {}
}