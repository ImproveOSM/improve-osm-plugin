/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
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
import org.openstreetmap.josm.plugins.improveosm.util.pref.entity.OnewayConfidenceLevelEntry;
import org.openstreetmap.josm.plugins.improveosm.util.pref.entity.TileTypeEntry;
import org.openstreetmap.josm.plugins.improveosm.util.pref.entity.TurnConfidenceLevelEntry;
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

        final List<OnewayConfidenceLevelEntry> entries =
                StructUtils.getListOfStructs(Config.getPref(), DOF_CONFIDENCE_LEVEL, OnewayConfidenceLevelEntry.class);
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
        final String comment = Config.getPref().get(MG_LAST_COMMENT);
        return comment == null ? "" : comment;
    }

    MissingGeometryFilter loadMissingGeometryFilter() {
        final Status status = loadStatusFilter(MG_STATUS);
        final List<TileTypeEntry> entriesVal =
                StructUtils.getListOfStructs(Config.getPref(), Keys.MG_TYPE, TileTypeEntry.class);

        EnumSet<TileType> types = null;
        if (entriesVal != null && !entriesVal.isEmpty()) {
            types = EnumSet.noneOf(TileType.class);
            for (final TileTypeEntry entry : entriesVal) {
                types.add(TileType.valueOf(entry.getName()));
            }
        }

        String valueStr =
                Util.zoom(MainApplication.getMap().mapView
                        .getRealBounds()) > org.openstreetmap.josm.plugins.improveosm.util.cnf.Config.getInstance()
                                .getMaxClusterZoom() ? Config.getPref().get(MG_TRIP_COUNT)
                                        : Config.getPref().get(MG_POINT_COUNT);
                valueStr = valueStr.trim();
                final Integer count = !valueStr.isEmpty() ? Integer.valueOf(valueStr) : null;
                return status == null && types == null ? MissingGeometryFilter.DEFAULT
                        : new MissingGeometryFilter(status, types, count);
    }


    /* TurnRestrictionLayer related methods */

    String loadTurnRestrictionLastComment() {
        final String comment = Config.getPref().get(TR_LAST_COMMENT);
        return comment == null ? "" : comment;
    }

    TurnRestrictionFilter loadTurnRestrictionFilter() {
        final Status status = loadStatusFilter(TR_STATUS);
        final List<TurnConfidenceLevelEntry> entries =
                StructUtils.getListOfStructs(Config.getPref(), TR_CONFIDENCE_LEVEL, TurnConfidenceLevelEntry.class);
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