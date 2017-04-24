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
import java.util.EnumSet;
import java.util.List;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.OnewayFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.TurnRestrictionFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.LocationPref;
import org.openstreetmap.josm.plugins.improveosm.entity.OnewayConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.TileType;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.pref.entity.OnewayConfidenceLevelEntry;
import org.openstreetmap.josm.plugins.improveosm.util.pref.entity.TileTypeEntry;
import org.openstreetmap.josm.plugins.improveosm.util.pref.entity.TurnConfidenceLevelEntry;


/**
 * Helper class, manages the load operations of the preference variables. The preference variables are loaded from the
 * global preference file.
 *
 * @author Beata
 * @version $Revision$
 */
final class LoadManager {

    String loadOsmUsername() {
        final String username = Main.pref.get(OSM_USERNAME);
        return username == null ? "" : username;
    }

    boolean loadErrorSuppressFlag() {
        return Main.pref.getBoolean(SUPPRESS_ERROR);
    }


    LocationPref loadLocationPrefOption() {
        final String location = Main.pref.get(LOCATION_PREF_OPTION);
        return location != null && !location.isEmpty() ? LocationPref.valueOf(location) : LocationPref.COPY_LOCATION;
    }

    String loadLocationPrefValue() {
        return Main.pref.get(LOCATION_PREF_VALUE);
    }

    boolean loadLocationTipSuppressFlag() {
        return Main.pref.getBoolean(LOCATION_TIP_SUPPRESS);
    }

    /* DirectionOfFlowLayer related methods */

    boolean loadDirectionOfFlowTipSuppressFlag() {
        return Main.pref.getBoolean(DOF_TIP_SUPPRESS);
    }

    String loadDirectionOfFlowLastComment() {
        final String comment = Main.pref.get(DOF_LAST_COMMENT);
        return comment == null ? "" : comment;
    }

    OnewayFilter loadOnewayFilter() {
        final Status status = loadStatusFilter(DOF_STATUS);

        final List<OnewayConfidenceLevelEntry> entries =
                Main.pref.getListOfStructs(DOF_CONFIDENCE_LEVEL, OnewayConfidenceLevelEntry.class);
        EnumSet<OnewayConfidenceLevel> confidenceLevels = null;
        if (entries != null && !entries.isEmpty()) {
            confidenceLevels = EnumSet.noneOf(OnewayConfidenceLevel.class);
            for (final OnewayConfidenceLevelEntry entry : entries) {
                confidenceLevels.add(OnewayConfidenceLevel.valueOf(entry.getName()));
            }
        }
        return status == null && confidenceLevels == null ? OnewayFilter.DEFAULT
                : new OnewayFilter(status, confidenceLevels);
    }

    /* MissingGeometyLayer related methods */

    String loadMissingGeometryLastComment() {
        final String comment = Main.pref.get(MG_LAST_COMMENT);
        return comment == null ? "" : comment;
    }

    MissingGeometryFilter loadMissingGeometryFilter() {
        final Status status = loadStatusFilter(MG_STATUS);
        final List<TileTypeEntry> entries = Main.pref.getListOfStructs(Keys.MG_TYPE, TileTypeEntry.class);
        EnumSet<TileType> types = null;
        if (entries != null && !entries.isEmpty()) {
            types = EnumSet.noneOf(TileType.class);
            for (final TileTypeEntry entry : entries) {
                types.add(TileType.valueOf(entry.getName()));
            }
        }

        String valueStr = Util.zoom(Main.map.mapView.getRealBounds()) > Config.getInstance().getMaxClusterZoom()
                ? Main.pref.get(MG_TRIP_COUNT) : Main.pref.get(MG_POINT_COUNT);
                valueStr = valueStr.trim();
                final Integer count = !valueStr.isEmpty() ? Integer.valueOf(valueStr) : null;
                return status == null && types == null ? MissingGeometryFilter.DEFAULT
                        : new MissingGeometryFilter(status, types, count);
    }

    /* TurnRestrictionLayer related methods */

    String loadTurnRestrictionLastComment() {
        final String comment = Main.pref.get(TR_LAST_COMMENT);
        return comment == null ? "" : comment;
    }

    TurnRestrictionFilter loadTurnRestrictionFilter() {
        final Status status = loadStatusFilter(TR_STATUS);
        final List<TurnConfidenceLevelEntry> entries =
                Main.pref.getListOfStructs(TR_CONFIDENCE_LEVEL, TurnConfidenceLevelEntry.class);
        EnumSet<TurnConfidenceLevel> confidenceLevels = null;
        if (entries != null && !entries.isEmpty()) {
            confidenceLevels = EnumSet.noneOf(TurnConfidenceLevel.class);
            for (final TurnConfidenceLevelEntry entry : entries) {
                confidenceLevels.add(TurnConfidenceLevel.valueOf(entry.getName()));
            }
        }
        return status == null && confidenceLevels == null ? TurnRestrictionFilter.DEFAULT
                : new TurnRestrictionFilter(status, confidenceLevels);
    }

    boolean loadMissingGeometryLayerOpenedFlag() {
        final String layerOpened = Main.pref.get(MG_LAYER_OPENED);
        return layerOpened.isEmpty() ? false : Boolean.valueOf(layerOpened);
    }

    boolean loadDirectionOfFlowLayerOpenedFlag() {
        final String layerOpened = Main.pref.get(DOF_LAYER_OPENED);
        return layerOpened.isEmpty() ? false : Boolean.valueOf(layerOpened);
    }

    boolean loadTurnRestrictionLayerOpenedFlag() {
        final String layerOpened = Main.pref.get(TR_LAYER_OPENED);
        return layerOpened.isEmpty() ? false : Boolean.valueOf(layerOpened);
    }

    boolean loadPanelOpenedFlag() {
        final String panelOpened = Main.pref.get(PANEL_OPENED);
        return panelOpened.isEmpty() ? false : Boolean.valueOf(panelOpened);
    }

    /* commonly used method */

    private Status loadStatusFilter(final String key) {
        final String statusStr = Main.pref.get(key);
        return statusStr != null && !statusStr.isEmpty() ? Status.valueOf(statusStr) : null;
    }
}