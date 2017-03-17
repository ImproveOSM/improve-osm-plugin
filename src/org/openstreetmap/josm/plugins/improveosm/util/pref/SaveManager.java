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
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.OLD_PLUGINS_WARNING_SUPPRESS;
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
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.OnewayFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.TurnRestrictionFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.LocationPref;
import org.openstreetmap.josm.plugins.improveosm.entity.OnewayConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.entity.TileType;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.pref.entity.OnewayConfidenceLevelEntry;
import org.openstreetmap.josm.plugins.improveosm.util.pref.entity.TileTypeEntry;
import org.openstreetmap.josm.plugins.improveosm.util.pref.entity.TurnConfidenceLevelEntry;


/**
 * Helper class, manages the save operations of the plugin preference variables. The preference variables are saved into
 * a global preference file.
 *
 * @author Beata
 * @version $Revision$
 */
final class SaveManager {

    void saveOsmUsername(final String username) {
        Main.pref.put(OSM_USERNAME, username);
    }

    void saveErrorSuppressFlag(final boolean flag) {
        Main.pref.put(SUPPRESS_ERROR, flag);
    }

    void saveOldPluginsWarningSuppressFlag(final boolean flag) {
        Main.pref.put(OLD_PLUGINS_WARNING_SUPPRESS, flag);
    }

    void saveLocationPrefOption(final LocationPref locationPref) {
        Main.pref.put(LOCATION_PREF_OPTION, locationPref.name());
    }

    void saveLocationPrefValue(final String value) {
        Main.pref.put(LOCATION_PREF_VALUE, value);
    }

    /* DirrectionOfFlow layer related methods */

    void saveDirectionOfFlowTipSuppressFlag(final boolean flag) {
        Main.pref.put(DOF_TIP_SUPPRESS, flag);
    }

    void saveDirectionOfFlowLastComment(final String comment) {
        Main.pref.put(DOF_LAST_COMMENT, comment);
    }

    void saveOnewayFilter(final OnewayFilter filter) {
        if (filter != null) {
            final String status = filter.getStatus() != null ? filter.getStatus().name() : "";
            Main.pref.put(DOF_STATUS, status);

            // confidence levels
            final List<OnewayConfidenceLevelEntry> entries = new ArrayList<>();
            if (filter.getConfidenceLevels() != null) {
                for (final OnewayConfidenceLevel confidence : filter.getConfidenceLevels()) {
                    entries.add(new OnewayConfidenceLevelEntry(confidence));
                }
            }
            Main.pref.putListOfStructs(DOF_CONFIDENCE_LEVEL, entries, OnewayConfidenceLevelEntry.class);
        }
    }

    void saveDirectionOfFlowFiltersChangedFlag(final boolean changed) {
        Main.pref.put(DOF_FILTERS_CHANGED, "");
        Main.pref.put(DOF_FILTERS_CHANGED, Boolean.toString(changed));
    }


    /* MissingGeometry layer related methods */

    void saveMissingGeoLastComment(final String comment) {
        Main.pref.put(MG_LAST_COMMENT, comment);
    }

    void saveMissingGeoFilter(final MissingGeometryFilter filter) {
        if (filter != null) {
            // status
            final String status = filter.getStatus() != null ? filter.getStatus().name() : "";
            Main.pref.put(MG_STATUS, status);

            // type
            final List<TileTypeEntry> entries = new ArrayList<>();
            if (filter.getTypes() != null) {
                for (final TileType type : filter.getTypes()) {
                    entries.add(new TileTypeEntry(type));
                }
            }
            Main.pref.putListOfStructs(MG_TYPE, entries, TileTypeEntry.class);
            final String countKey =
                    Util.zoom(Main.map.mapView.getRealBounds()) > Config.getInstance().getMaxClusterZoom()
                            ? MG_TRIP_COUNT : MG_POINT_COUNT;
            final String count = filter.getCount() != null ? filter.getCount().toString() : "";
            Main.pref.put(countKey, count);
        }
    }

    void saveMissingGeometryFiltersChangedFlag(final boolean changed) {
        Main.pref.put(MG_FILTERS_CHANGED, "");
        Main.pref.put(MG_FILTERS_CHANGED, Boolean.toString(changed));
    }


    /* TurnRestriction layer related methods */

    void saveTurnRestrictionLastComment(final String comment) {
        Main.pref.put(TR_LAST_COMMENT, comment);
    }

    void saveTurnRestrictionFilter(final TurnRestrictionFilter filter) {
        if (filter != null) {
            // status
            final String status = filter.getStatus() != null ? filter.getStatus().name() : "";
            Main.pref.put(TR_STATUS, status);

            // confidence levels
            final List<TurnConfidenceLevelEntry> entries = new ArrayList<>();
            if (filter.getConfidenceLevels() != null) {
                for (final TurnConfidenceLevel confidence : filter.getConfidenceLevels()) {
                    entries.add(new TurnConfidenceLevelEntry(confidence));
                }
            }
            Main.pref.putListOfStructs(TR_CONFIDENCE_LEVEL, entries, TurnConfidenceLevelEntry.class);
        }
    }

    void saveTurnRestrictionFiltersChangedFlag(final boolean changed) {
        Main.pref.put(TR_FILTERS_CHANGED, "");
        Main.pref.put(TR_FILTERS_CHANGED, Boolean.toString(changed));
    }

    void saveLocationTipSuppressFlag(final boolean tag) {
        Main.pref.put(LOCATION_TIP_SUPPRESS, tag);
    }

    void saveMissingGeometryLayerOpenedFlag(final boolean layerOpened) {
        Main.pref.put(MG_LAYER_OPENED, layerOpened);
    }

    void saveDirectionOfFlowLayerOpenedFlag(final boolean layerOpened) {
        Main.pref.put(DOF_LAYER_OPENED, layerOpened);
    }

    void saveTurnRestrictionLayerOpenedFlag(final boolean layerOpened) {
        Main.pref.put(TR_LAYER_OPENED, layerOpened);
    }

    void savePanelOpenedFlag(final boolean panelOpened) {
        Main.pref.put(PANEL_OPENED, panelOpened);
    }

}