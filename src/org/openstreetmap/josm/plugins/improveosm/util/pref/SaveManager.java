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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.OnewayFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.TurnRestrictionFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.DataLayer;
import org.openstreetmap.josm.plugins.improveosm.entity.OnewayConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.entity.TileType;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.util.pref.entity.DataLayerEntry;
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
        Main.pref.put(Keys.OSM_USERNAME, username);
    }

    void saveErrorSuppressFlag(final boolean flag) {
        Main.pref.put(Keys.SUPPRESS_ERROR, flag);
    }

    void saveDataLayers(final EnumSet<DataLayer> dataLayers) {
        final List<DataLayerEntry> entries = new ArrayList<>();
        if (dataLayers != null) {
            for (final DataLayer layer : dataLayers) {
                entries.add(new DataLayerEntry(layer));
            }
        }
        Main.pref.putListOfStructs(Keys.DATA_LAYER, entries, DataLayerEntry.class);
    }

    /* DirrectionOfFlow layer related methods */


    void saveDirectionOfFlowTipSuppressFlag(final boolean flag) {
        Main.pref.put(Keys.DIRECTIONOFFLOW_TIP_SUPPRESS, flag);
    }

    void saveDirectionOfFlowLastComment(final String comment) {
        Main.pref.put(Keys.DIRECTIONOFFLOW_LAST_COMMENT, comment);
    }

    void saveOnewayFilter(final OnewayFilter filter) {
        if (filter != null) {
            final String status = filter.getStatus() != null ? filter.getStatus().name() : "";
            Main.pref.put(Keys.DIRECTIONOFFLOW_STATUS, status);

            // confidence levels
            final List<OnewayConfidenceLevelEntry> entries = new ArrayList<OnewayConfidenceLevelEntry>();
            if (filter.getConfidenceLevels() != null) {
                for (final OnewayConfidenceLevel confidence : filter.getConfidenceLevels()) {
                    entries.add(new OnewayConfidenceLevelEntry(confidence));
                }
            }
            Main.pref.putListOfStructs(Keys.DIRECTIONOFFLOW_CONFIDENCE_LEVEL, entries,
                    OnewayConfidenceLevelEntry.class);
        }
    }

    void saveDirectionOfFlowFiltersChangedFlag(final boolean changed) {
        Main.pref.put(Keys.DIRECTIONOFFLOW_FILTERS_CHANGED, "");
        Main.pref.put(Keys.DIRECTIONOFFLOW_FILTERS_CHANGED, "" + changed);
    }


    /* MissingGeometry layer related methods */

    void saveMissingGeoLastComment(final String comment) {
        Main.pref.put(Keys.MISSINGGEO_LAST_COMMENT, comment);
    }

    void saveMissingGeoFilter(final MissingGeometryFilter filter) {
        if (filter != null) {
            // status
            final String status = filter.getStatus() != null ? filter.getStatus().name() : "";
            Main.pref.put(Keys.MISSINGGEO_STATUS, status);

            // type
            final List<TileTypeEntry> entries = new ArrayList<TileTypeEntry>();
            if (filter.getTypes() != null) {
                for (final TileType type : filter.getTypes()) {
                    entries.add(new TileTypeEntry(type));
                }
            }
            Main.pref.putListOfStructs(Keys.MISSINGGEO_TYPE, entries, TileTypeEntry.class);
            final String count = filter.getCount() != null ? filter.getCount().toString() : "";
            Main.pref.put(Keys.MISSINGGEO_COUNT, count);
        }
    }

    void saveMissingGeometryFiltersChangedFlag(final boolean changed) {
        Main.pref.put(Keys.MISSINGGEO_FILTERS_CHANGED, "");
        Main.pref.put(Keys.MISSINGGEO_FILTERS_CHANGED, "" + changed);
    }


    /* TurnRestriction layer related methods */

    void saveTurnRestrictionLastComment(final String comment) {
        Main.pref.put(Keys.TURN_RESTRICTION_LAST_COMMENT, comment);
    }

    void saveTurnRestrictionFilter(final TurnRestrictionFilter filter) {
        if (filter != null) {
            // status
            final String status = filter.getStatus() != null ? filter.getStatus().name() : "";
            Main.pref.put(Keys.TURN_RESTRICTION_STATUS, status);

            // confidence levels
            final List<TurnConfidenceLevelEntry> entries = new ArrayList<TurnConfidenceLevelEntry>();
            if (filter.getConfidenceLevels() != null) {
                for (final TurnConfidenceLevel confidence : filter.getConfidenceLevels()) {
                    entries.add(new TurnConfidenceLevelEntry(confidence));
                }
            }
            Main.pref.putListOfStructs(Keys.TURN_RESTRICTION_CONFIDENCE_LEVEL, entries, TurnConfidenceLevelEntry.class);
        }
    }

    void saveTurnRestrictionFiltersChangedFlag(final boolean changed) {
        Main.pref.put(Keys.TURN_RESTRICTION_FILTERS_CHANGED, "");
        Main.pref.put(Keys.TURN_RESTRICTION_FILTERS_CHANGED, "" + changed);
    }
}