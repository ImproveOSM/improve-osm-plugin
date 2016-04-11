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

import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.OnewayFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.TurnRestrictionFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.DataLayer;
import org.openstreetmap.josm.plugins.improveosm.entity.OnewayConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.TileType;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.pref.entity.DataLayerEntry;
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
        final String username = Main.pref.get(Keys.OSM_USERNAME);
        return username == null ? "" : username;
    }

    boolean loadErrorSuppressFlag() {
        return Main.pref.getBoolean(Keys.SUPPRESS_ERROR);
    }

    boolean loadOldPluginsFlag() {
        final Collection<String> plugins = Main.pref.getCollection(Keys.PLUGINS);
        return plugins.contains("missingRoads") || plugins.contains("trafficFlowDirection");
    }

    boolean loadOldPluginsWarningSuppressFlag() {
        return Main.pref.getBoolean(Keys.OLD_PLUGINS_WARNING_SUPPRESS);
    }

    EnumSet<DataLayer> loadDataLayers() {
        final List<DataLayerEntry> entries = Main.pref.getListOfStructs(Keys.DATA_LAYER, DataLayerEntry.class);
        final EnumSet<DataLayer> enabledLayers = Config.getInstance().getEnabledDataLayers();
        EnumSet<DataLayer> dataLayers = null;
        if (entries != null && !entries.isEmpty()) {
            dataLayers = EnumSet.noneOf(DataLayer.class);
            for (final DataLayerEntry entry : entries) {
                final DataLayer layer = DataLayer.valueOf(entry.getName());
                if (enabledLayers.contains(layer)) {
                    dataLayers.add(DataLayer.valueOf(entry.getName()));
                }
            }
        }
        return dataLayers == null || dataLayers.isEmpty() ? enabledLayers : dataLayers;
    }

    /* DirectionOfFlowLayer related methods */

    boolean loadDirectionOfFlowTipSuppressFlag() {
        return Main.pref.getBoolean(Keys.DIRECTIONOFFLOW_TIP_SUPPRESS);
    }

    String loadDirectionOfFlowLastComment() {
        final String comment = Main.pref.get(Keys.DIRECTIONOFFLOW_LAST_COMMENT);
        return comment == null ? "" : comment;
    }

    OnewayFilter loadOnewayFilter() {
        final Status status = loadStatusFilter(Keys.DIRECTIONOFFLOW_STATUS);

        final List<OnewayConfidenceLevelEntry> entries =
                Main.pref.getListOfStructs(Keys.DIRECTIONOFFLOW_CONFIDENCE_LEVEL, OnewayConfidenceLevelEntry.class);
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
        final String comment = Main.pref.get(Keys.MISSINGGEO_LAST_COMMENT);
        return comment == null ? "" : comment;
    }

    MissingGeometryFilter loadMissingGeometryFilter() {
        final Status status = loadStatusFilter(Keys.MISSINGGEO_STATUS);
        final List<TileTypeEntry> entries = Main.pref.getListOfStructs(Keys.MISSINGGEO_TYPE, TileTypeEntry.class);
        EnumSet<TileType> types = null;
        if (entries != null && !entries.isEmpty()) {
            types = EnumSet.noneOf(TileType.class);
            for (final TileTypeEntry entry : entries) {
                types.add(TileType.valueOf(entry.getName()));
            }
        }

        String valueStr = Util.zoom(Main.map.mapView.getRealBounds()) > Config.getInstance().getMaxClusterZoom()
                ? Main.pref.get(Keys.MISSINGGEO_TRIP_COUNT) : Main.pref.get(Keys.MISSINGGEO_POINT_COUNT);
                valueStr = valueStr.trim();
                final Integer count = (valueStr != null && !valueStr.isEmpty()) ? Integer.valueOf(valueStr) : null;
                return status == null && types == null ? MissingGeometryFilter.DEFAULT
                        : new MissingGeometryFilter(status, types, count);
    }

    /* TurnRestrictionLayer related methods */

    String loadTurnRestrictionLastComment() {
        final String comment = Main.pref.get(Keys.TURN_RESTRICTION_LAST_COMMENT);
        return comment == null ? "" : comment;
    }

    TurnRestrictionFilter loadTurnRestrictionFilter() {
        final Status status = loadStatusFilter(Keys.TURN_RESTRICTION_STATUS);
        final List<TurnConfidenceLevelEntry> entries =
                Main.pref.getListOfStructs(Keys.TURN_RESTRICTION_CONFIDENCE_LEVEL, TurnConfidenceLevelEntry.class);
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

    /* commonly used method */

    private Status loadStatusFilter(final String key) {
        final String statusStr = Main.pref.get(key);
        return statusStr != null && !statusStr.isEmpty() ? Status.valueOf(statusStr) : null;
    }
}