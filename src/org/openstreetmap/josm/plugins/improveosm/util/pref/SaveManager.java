/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.util.pref;

import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.DOF_CONFIDENCE_LEVEL;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.DOF_FILTERS_CHANGED;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.DOF_LAST_COMMENT;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.DOF_LAYER_OPENED;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.DOF_STATUS;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.DOF_TIP_SUPPRESS;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.LOCATION_PREF_OPTION;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.LOCATION_PREF_VALUE;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.LOCATION_TIP_SUPPRESS;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.MG_FILTERS_CHANGED;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.MG_LAST_COMMENT;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.MG_LAYER_OPENED;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.MG_POINT_COUNT;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.MG_STATUS;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.MG_TRIP_COUNT;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.MG_TYPE;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.OSM_USERNAME;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.PANEL_OPENED;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.SUPPRESS_ERROR;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.TR_CONFIDENCE_LEVEL;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.TR_FILTERS_CHANGED;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.TR_LAST_COMMENT;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.TR_LAYER_OPENED;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.TR_STATUS;
import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.josm.data.StructUtils;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.OnewayFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.TurnRestrictionFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.LocationPref;
import org.openstreetmap.josm.plugins.improveosm.entity.OnewayConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.entity.TileType;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.pref.entity.OnewayConfidenceLevelEntry;
import org.openstreetmap.josm.plugins.improveosm.util.pref.entity.TileTypeEntry;
import org.openstreetmap.josm.plugins.improveosm.util.pref.entity.TurnConfidenceLevelEntry;
import org.openstreetmap.josm.spi.preferences.Config;


/**
 * Helper class, manages the save operations of the plugin preference variables. The preference variables are saved into
 * a global preference file.
 *
 * @author Beata
 * @version $Revision$
 */
final class SaveManager {

    void saveOsmUsername(final String username) {
        Config.getPref().put(OSM_USERNAME, username);
    }

    void saveErrorSuppressFlag(final boolean flag) {
        Config.getPref().putBoolean(SUPPRESS_ERROR, flag);
    }

    void saveLocationPrefOption(final LocationPref locationPref) {
        Config.getPref().put(LOCATION_PREF_OPTION, locationPref.name());
    }

    void saveLocationPrefValue(final String value) {
        Config.getPref().put(LOCATION_PREF_VALUE, value);
    }

    /* DirrectionOfFlow layer related methods */

    void saveDirectionOfFlowTipSuppressFlag(final boolean flag) {
        Config.getPref().putBoolean(DOF_TIP_SUPPRESS, flag);
    }

    void saveDirectionOfFlowLastComment(final String comment) {
        Config.getPref().put(DOF_LAST_COMMENT, comment);
    }


    void saveOnewayFilter(final OnewayFilter filter) {
        if (filter != null) {
            final String status = filter.getStatus() != null ? filter.getStatus().name() : "";
            Config.getPref().put(DOF_STATUS, status);

            // confidence levels
            final List<OnewayConfidenceLevelEntry> entries = new ArrayList<>();
            if (filter.getConfidenceLevels() != null) {
                for (final OnewayConfidenceLevel confidence : filter.getConfidenceLevels()) {
                    entries.add(new OnewayConfidenceLevelEntry(confidence));
                }
            }
            StructUtils.putListOfStructs(Config.getPref(), DOF_CONFIDENCE_LEVEL, entries,
                    OnewayConfidenceLevelEntry.class);
        }
    }

    void saveDirectionOfFlowFiltersChangedFlag(final boolean changed) {
        Config.getPref().put(DOF_FILTERS_CHANGED, "");
        Config.getPref().put(DOF_FILTERS_CHANGED, Boolean.toString(changed));
    }


    /* MissingGeometry layer related methods */

    void saveMissingGeoLastComment(final String comment) {
        Config.getPref().put(MG_LAST_COMMENT, comment);
    }


    void saveMissingGeoFilter(final MissingGeometryFilter filter) {
        if (filter != null) {
            // status
            final String status = filter.getStatus() != null ? filter.getStatus().name() : "";
            Config.getPref().put(MG_STATUS, status);

            // type
            final List<TileTypeEntry> entries = new ArrayList<>();
            if (filter.getTypes() != null) {
                for (final TileType type : filter.getTypes()) {
                    entries.add(new TileTypeEntry(type));
                }
            }
            StructUtils.putListOfStructs(Config.getPref(), MG_TYPE, entries, TileTypeEntry.class);
            final String countKey = Util.zoom(MainApplication.getMap().mapView
                    .getRealBounds()) > org.openstreetmap.josm.plugins.improveosm.util.cnf.Config.getInstance()
                    .getMaxClusterZoom() ? MG_TRIP_COUNT : MG_POINT_COUNT;
            final String count = filter.getCount() != null ? filter.getCount().toString() : "";
            Config.getPref().put(countKey, count);
        }
    }

    void saveMissingGeometryFiltersChangedFlag(final boolean changed) {
        Config.getPref().put(MG_FILTERS_CHANGED, "");
        Config.getPref().put(MG_FILTERS_CHANGED, Boolean.toString(changed));
    }


    /* TurnRestriction layer related methods */

    void saveTurnRestrictionLastComment(final String comment) {
        Config.getPref().put(TR_LAST_COMMENT, comment);
    }

    void saveTurnRestrictionFilter(final TurnRestrictionFilter filter) {
        if (filter != null) {
            // status
            final String status = filter.getStatus() != null ? filter.getStatus().name() : "";
            Config.getPref().put(TR_STATUS, status);

            // confidence levels
            final List<TurnConfidenceLevelEntry> entries = new ArrayList<>();
            if (filter.getConfidenceLevels() != null) {
                for (final TurnConfidenceLevel confidence : filter.getConfidenceLevels()) {
                    entries.add(new TurnConfidenceLevelEntry(confidence));
                }
            }
            StructUtils.putListOfStructs(Config.getPref(), TR_CONFIDENCE_LEVEL, entries,
                    TurnConfidenceLevelEntry.class);
        }
    }

    void saveTurnRestrictionFiltersChangedFlag(final boolean changed) {
        Config.getPref().put(TR_FILTERS_CHANGED, "");
        Config.getPref().put(TR_FILTERS_CHANGED, Boolean.toString(changed));
    }

    void saveLocationTipSuppressFlag(final boolean tag) {
        Config.getPref().putBoolean(LOCATION_TIP_SUPPRESS, tag);
    }

    void saveMissingGeometryLayerOpenedFlag(final boolean layerOpened) {
        Config.getPref().putBoolean(MG_LAYER_OPENED, layerOpened);
    }

    void saveDirectionOfFlowLayerOpenedFlag(final boolean layerOpened) {
        Config.getPref().putBoolean(DOF_LAYER_OPENED, layerOpened);
    }

    void saveTurnRestrictionLayerOpenedFlag(final boolean layerOpened) {
        Config.getPref().putBoolean(TR_LAYER_OPENED, layerOpened);
    }

    void savePanelOpenedFlag(final boolean panelOpened) {
        Config.getPref().putBoolean(PANEL_OPENED, panelOpened);
    }

}