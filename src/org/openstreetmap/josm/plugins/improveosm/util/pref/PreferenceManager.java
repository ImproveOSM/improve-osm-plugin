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

import java.util.EnumSet;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.OnewayFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.DataLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.DirectionOfFlowLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.MissingGeometryLayer;


/**
 * Utility class, manages save and load (put & get) operations of the preference variables. The preference variables are
 * saved into a global preference file. Preference variables are static variables which can be accessed from any plugin
 * class. Values saved in this global file, can be accessed also after a JOSM restart.
 *
 * @author Beata
 * @version $Revision$
 */
public final class PreferenceManager {

    private static final PreferenceManager INSTANCE = new PreferenceManager();

    private final LoadManager loadManager;
    private final SaveManager saveManager;

    private PreferenceManager() {
        loadManager = new LoadManager();
        saveManager = new SaveManager();
    }

    public static PreferenceManager getInstance() {
        return INSTANCE;
    }

    /**
     * Loads the user's OSM username.
     *
     * @return a {@code String} object
     */
    public String loadOsmUsername() {
        return loadManager.loadOsmUsername();
    }

    /**
     * Saves the user's OSM username.
     *
     * @param username a {@code String} value
     */
    public void saveOsmUsername(final String username) {
        saveManager.saveOsmUsername(username);
    }

    /**
     * Loads the error suppress flag. If this value is true, then all the future service errors will be suppressed.
     *
     * @return a boolean value
     */
    public boolean loadErrorSuppressFlag() {
        return loadManager.loadErrorSuppressFlag();
    }

    /**
     * Saves the error suppress flag to the preference file.
     *
     * @param flag a boolean value
     */
    public void saveErrorSuppressFlag(final boolean flag) {
        saveManager.saveErrorSuppressFlag(flag);
    }

    /**
     * Loads the selected data layers.
     *
     * @return a set of {@code DataLayer}
     */
    public EnumSet<DataLayer> loadDataLayers() {
        return loadManager.loadDataLayers();
    }

    /**
     * Saves the selected data layers to the preference file.
     *
     * @param dataLayers a set of {@code DataLayer}s
     */
    public void saveDataLayers(final EnumSet<DataLayer> dataLayers) {
        saveManager.saveDataLayers(dataLayers);
    }

    /**
     * Saves the given flag to the preference file.
     *
     * @param changed a boolean value
     */
    public void saveDirectionOfFlowFiltersChangedFlag(final boolean changed) {
        saveManager.saveDirectionOfFlowFiltersChangedFlag(changed);
    }

    /**
     * Loads the tip suppress flag from the preference file.
     *
     * @return a boolean value
     */
    public boolean loadDirectionOfFlowTipSuppressFlag() {
        return loadManager.loadDirectionOfFlowTipSuppressFlag();
    }

    /**
     * Saves the tip suppress flag to the preference file.
     *
     * @param flag a boolean value
     */
    public void saveDirectionOfFlowTipSuppressFlag(final boolean flag) {
        saveManager.saveDirectionOfFlowTipSuppressFlag(flag);
    }

    /**
     * Loads the last comment from the preference file, based on the currently active layer.
     *
     * @return a {@code String} value
     */
    public String loadLastComment() {
        String lastComment = null;
        final Layer activeLayer = Main.map.mapView.getActiveLayer();
        if (activeLayer instanceof DirectionOfFlowLayer) {
            lastComment = loadManager.loadDirectionOfFlowLastComment();
        } else if (activeLayer instanceof MissingGeometryLayer) {
            lastComment = loadManager.loadMissingGeometryLastComment();
        }
        return lastComment;
    }

    /**
     * Saves the user's last comment used for the DirectionOfFlow layer.
     *
     * @param comment a {@code String}
     */
    public void saveDirectionOfFlowLastComment(final String comment) {
        saveManager.saveDirectionOfFlowLastComment(comment);
    }

    /**
     * Loads the oneway filter used by the DirectionOfFlow layer. If no filter is set, then the method returns
     * {@code OnewayFilter#DEFAULT} filter.
     *
     * @return a {@code OnewayFilter} object.
     */
    public OnewayFilter loadOnewayFilter() {
        return loadManager.loadOnewayFilter();
    }

    /**
     * Saves the oneway filter user by the DirectionOfFlow layer into the preference file.
     *
     * @param filter a {@code Oneway} filter
     */
    public void saveOnewayFilter(final OnewayFilter filter) {
        saveManager.saveOnewayFilter(filter);
    }

    /**
     * Saves the user's last comment used for the MissingGeometry layer.
     *
     * @param comment a {@code String}
     */
    public void saveMissingGeometryLastComment(final String comment) {
        saveManager.saveMissingGeoLastComment(comment);
    }

    /**
     * Saves the given flag to the preference file.
     *
     * @param changed a boolean value
     */
    public void saveMissingGeometryFiltersChangedFlag(final boolean changed) {
        saveManager.saveMissingGeometryFiltersChangedFlag(changed);
    }

    /**
     * Loads the missing geometry filter. If no filter is set, then the method returns
     * {@code MissingGeometryFilter#DEFAULT} filter.
     *
     * @return a {@code MissingGeometryFilter} object
     */
    public MissingGeometryFilter loadMissingGeometryFilter() {
        return loadManager.loadMissingGeometryFilter();
    }

    /**
     * Saves the missing geometry filter to the preference file.
     *
     * @param filter a {@code MissingGeometryFilter} object
     */
    public void saveMissingGeometryFilter(final MissingGeometryFilter filter) {
        saveManager.saveMissingGeoFilter(filter);
    }

}