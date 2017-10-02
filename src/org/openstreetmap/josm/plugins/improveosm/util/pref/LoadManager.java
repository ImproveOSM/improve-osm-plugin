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

import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.DOF_CONFIDENCE_LEVEL;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.DOF_LAST_COMMENT;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.DOF_LAYER_OPENED;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.DOF_STATUS;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.DOF_TIP_SUPPRESS;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.LOCATION_PREF_OPTION;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.LOCATION_PREF_VALUE;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.LOCATION_TIP_SUPPRESS;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.MG_LAST_COMMENT;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.MG_LAYER_OPENED;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.MG_POINT_COUNT;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.MG_STATUS;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.MG_TRIP_COUNT;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.OSM_USERNAME;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.PANEL_OPENED;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.SUPPRESS_ERROR;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.TR_CONFIDENCE_LEVEL;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.TR_LAST_COMMENT;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.TR_LAYER_OPENED;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.TR_STATUS;
import java.util.HashSet;
import java.util.List;
import org.openstreetmap.josm.data.StructUtils;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.OnewayFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.TurnRestrictionFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.LocationPref;
import org.openstreetmap.josm.plugins.improveosm.entity.OnewayConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.TileType;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.spi.preferences.Config;


/**
 * Helper class, manages the load operations of the preference variables. The preference variables are loaded from the
 * global preference file.
 *
 * @author Beata
 * @version $Revision$
 */
final class LoadManager {

    String loadOsmUsername() {
        final String username = Config.getPref().get(OSM_USERNAME);
        return username == null ? "" : username;
    }

    boolean loadErrorSuppressFlag() {
        return Config.getPref().getBoolean(SUPPRESS_ERROR);
    }


    LocationPref loadLocationPrefOption() {
        final String location = Config.getPref().get(LOCATION_PREF_OPTION);
        return location != null && !location.isEmpty() ? LocationPref.valueOf(location) : LocationPref.COPY_LOCATION;
    }

    String loadLocationPrefValue() {
        return Config.getPref().get(LOCATION_PREF_VALUE);
    }

    boolean loadLocationTipSuppressFlag() {
        return Config.getPref().getBoolean(LOCATION_TIP_SUPPRESS);
    }

    /* DirectionOfFlowLayer related methods */

    boolean loadDirectionOfFlowTipSuppressFlag() {
        return Config.getPref().getBoolean(DOF_TIP_SUPPRESS);
    }

    String loadDirectionOfFlowLastComment() {
        final String comment = Config.getPref().get(DOF_LAST_COMMENT);
        return comment == null ? "" : comment;
    }

    OnewayFilter loadOnewayFilter() {
        final Status status = loadStatusFilter(DOF_STATUS);
        final List<OnewayConfidenceLevel> confidenceLevels =
                StructUtils.getListOfStructs(Config.getPref(), DOF_CONFIDENCE_LEVEL, OnewayConfidenceLevel.class);
        return status == null && confidenceLevels == null ? OnewayFilter.DEFAULT
                : new OnewayFilter(status, new HashSet<>(confidenceLevels));
    }

    /* MissingGeometyLayer related methods */

    String loadMissingGeometryLastComment() {
        final String comment = Config.getPref().get(MG_LAST_COMMENT);
        return comment == null ? "" : comment;
    }

    MissingGeometryFilter loadMissingGeometryFilter() {
        final Status status = loadStatusFilter(MG_STATUS);
        final List<TileType> types = StructUtils.getListOfStructs(Config.getPref(), Keys.MG_TYPE, TileType.class);
        String valueStr = Util.zoom(MainApplication.getMap().mapView
                .getRealBounds()) > org.openstreetmap.josm.plugins.improveosm.util.cnf.Config.getInstance()
                        .getMaxClusterZoom() ? Config.getPref().get(MG_TRIP_COUNT)
                                : Config.getPref().get(MG_POINT_COUNT);
        valueStr = valueStr.trim();
        final Integer count = !valueStr.isEmpty() ? Integer.valueOf(valueStr) : null;
        return status == null && types == null ? MissingGeometryFilter.DEFAULT
                : new MissingGeometryFilter(status, new HashSet<>(types), count);
    }

    /* TurnRestrictionLayer related methods */

    String loadTurnRestrictionLastComment() {
        final String comment = Config.getPref().get(TR_LAST_COMMENT);
        return comment == null ? "" : comment;
    }

    TurnRestrictionFilter loadTurnRestrictionFilter() {
        final Status status = loadStatusFilter(TR_STATUS);


        final List<TurnConfidenceLevel> confidenceLevels =
                StructUtils.getListOfStructs(Config.getPref(), TR_CONFIDENCE_LEVEL, TurnConfidenceLevel.class);
        return status == null && confidenceLevels == null ? TurnRestrictionFilter.DEFAULT
                : new TurnRestrictionFilter(status, new HashSet<>(confidenceLevels));
    }

    boolean loadMissingGeometryLayerOpenedFlag() {
        final String layerOpened = Config.getPref().get(MG_LAYER_OPENED);
        return layerOpened.isEmpty() ? false : Boolean.valueOf(layerOpened);
    }

    boolean loadDirectionOfFlowLayerOpenedFlag() {
        final String layerOpened = Config.getPref().get(DOF_LAYER_OPENED);
        return layerOpened.isEmpty() ? false : Boolean.valueOf(layerOpened);
    }

    boolean loadTurnRestrictionLayerOpenedFlag() {
        final String layerOpened = Config.getPref().get(TR_LAYER_OPENED);
        return layerOpened.isEmpty() ? false : Boolean.valueOf(layerOpened);
    }

    boolean loadPanelOpenedFlag() {
        final String panelOpened = Config.getPref().get(PANEL_OPENED);
        return panelOpened.isEmpty() ? false : Boolean.valueOf(panelOpened);
    }

    /* commonly used method */

    private Status loadStatusFilter(final String key) {
        final String statusStr = Config.getPref().get(key);
        return statusStr != null && !statusStr.isEmpty() ? Status.valueOf(statusStr) : null;
    }
}