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

import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.DOF_FILTERS_CHANGED;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.DOF_LAYER_OPENED;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.MG_FILTERS_CHANGED;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.MG_LAYER_OPENED;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.PANEL_ICON_VISIBILITY;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.TR_FILTERS_CHANGED;
import static org.openstreetmap.josm.plugins.improveosm.util.pref.Keys.TR_LAYER_OPENED;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.gui.layer.Layer;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.OnewayFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.TurnRestrictionFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.LocationPref;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.DirectionOfFlowLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.MissingGeometryLayer;
import org.openstreetmap.josm.plugins.improveosm.gui.layer.TurnRestrictionLayer;


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
     * Loads the selected location preference settings.
     *
     * @return a set of {@code LocationPref}
     */
    public LocationPref loadLocationPrefOption() {
        return loadManager.loadLocationPrefOption();
    }

    /**
     * Loads the saved url.
     *
     * @return a {@code String} representing the saved url.
     */
    public String loadLocationPrefValue() {
        return loadManager.loadLocationPrefValue();
    }

    /**
     * Saves the selected location button functionality to the preference file.
     *
     * @param pref a {@code LocationPref}
     */
    public void saveLocationPrefOption(final LocationPref pref) {
        saveManager.saveLocationPrefOption(pref);
    }

    /**
     * Saves the selected url for the location button functionality to the preference file.
     *
     * @param value a {@code String} representing the url.
     */
    public void saveLocationPrefValue(final String value) {
        saveManager.saveLocationPrefValue(value);
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
        final Layer activeLayer = Main.getLayerManager().getActiveLayer();
        if (activeLayer instanceof DirectionOfFlowLayer) {
            lastComment = loadManager.loadDirectionOfFlowLastComment();
        } else if (activeLayer instanceof MissingGeometryLayer) {
            lastComment = loadManager.loadMissingGeometryLastComment();
        } else if (activeLayer instanceof TurnRestrictionLayer) {
            lastComment = loadManager.loadTurnRestrictionLastComment();
        }
        return lastComment;
    }

    /**
     * Saves the user's last comment in the preference file based on the given active layer.
     *
     * @param activeLayer the current active layer
     * @param comment a {@code String} value
     */
    public void saveLastComment(final Layer activeLayer, final String comment) {
        if (activeLayer instanceof MissingGeometryLayer) {
            saveManager.saveMissingGeoLastComment(comment);
        } else if (activeLayer instanceof DirectionOfFlowLayer) {
            saveManager.saveDirectionOfFlowLastComment(comment);
        } else if (activeLayer instanceof TurnRestrictionLayer) {
            saveManager.saveTurnRestrictionLastComment(comment);
        }
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
     * Saves the one-way filter user by the DirectionOfFlow layer into the preference file.
     *
     * @param filter a {@code Oneway} filter
     */
    public void saveOnewayFilter(final OnewayFilter filter) {
        saveManager.saveOnewayFilter(filter);
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

    /**
     * Saves the given flag to the preference file.
     *
     * @param changed a boolean value
     */
    public void saveTurnRestrictionFiltersChangedFlag(final boolean changed) {
        saveManager.saveTurnRestrictionFiltersChangedFlag(changed);
    }

    /**
     * Loads the turn restriction filter. If no filter is set, then the method returns
     * {@code TurnRestrictionFilter#DEFAULT} filter.
     *
     * @return a {@code MissingGeometryFilter} object
     */
    public TurnRestrictionFilter loadTurnRestrictionFilter() {
        return loadManager.loadTurnRestrictionFilter();
    }

    /**
     * Saves the turn restriction filter to the preference file.
     *
     * @param filter a {@code TurnRestrictionFilter} object
     */
    public void saveTurnRestrictionFilter(final TurnRestrictionFilter filter) {
        saveManager.saveTurnRestrictionFilter(filter);
    }

    /**
     * Loads the location tip suppress flag. This flag return true if the user had chosen not seeing location button
     * tips anymore, false otherwise.
     *
     * @return a boolean value
     */
    public boolean loadLocationTipSuppressFlag() {
        return loadManager.loadLocationTipSuppressFlag();
    }

    /**
     * Save the location tip suppress flag to the preference file.
     *
     * @param flag a boolean value representing the value to be saved.
     */
    public void saveLocationTipSuppressFlag(final boolean flag) {
        saveManager.saveLocationTipSuppressFlag(flag);
    }

    public void saveMissingGeometryLayerOpenedFlag(final boolean layerOpened) {
        saveManager.saveMissingGeometryLayerOpenedFlag(layerOpened);
    }

    public boolean loadMissingGeometryLayerOpenedFlag() {
        return loadManager.loadMissingGeometryLayerOpenedFlag();
    }

    public void saveDirectionOfFlowLayerOpenedFlag(final boolean layerOpened) {
        saveManager.saveDirectionOfFlowLayerOpenedFlag(layerOpened);
    }

    public boolean loadDirectionOfFlowLayerOpenedFlag() {
        return loadManager.loadDirectionOfFlowLayerOpenedFlag();
    }

    public void saveTurnRestrictionLayerOpenedFlag(final boolean layerOpened) {
        saveManager.saveTurnRestrictionLayerOpenedFlag(layerOpened);
    }

    public boolean loadTurnRestrictionLayerOpenedFlag() {
        return loadManager.loadTurnRestrictionLayerOpenedFlag();
    }

    public void savePanelOpenedFlag(final String value) {
        final Boolean panelOpened = Boolean.parseBoolean(value);
        saveManager.savePanelOpenedFlag(panelOpened);
    }

    public boolean loadPanelOpenedFlag() {
        return loadManager.loadPanelOpenedFlag();
    }

    public boolean missingGeometryDataPreferencesChanged(final String key, final String newValue) {
        return (MG_LAYER_OPENED.equals(key) || MG_FILTERS_CHANGED.equals(key))
                && Boolean.TRUE.toString().equals(newValue);
    }

    public boolean directionOfFlowDataPreferencesChanged(final String key, final String newValue) {
        return (DOF_LAYER_OPENED.equals(key) || DOF_FILTERS_CHANGED.equals(key))
                && Boolean.TRUE.toString().equals(newValue);
    }

    public boolean turnRestrictionDataPreferencesChanged(final String key, final String newValue) {
        return (TR_LAYER_OPENED.equals(key) || TR_FILTERS_CHANGED.equals(key))
                && Boolean.TRUE.toString().equals(newValue);
    }

    public boolean isPanelIconVisibilityKey(final String key) {
        return PANEL_ICON_VISIBILITY.equals(key);
    }
}