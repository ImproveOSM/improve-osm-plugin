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

    public boolean loadErrorSuppressFlag() {
        return loadManager.loadErrorSuppressFlag();
    }

    public void saveErrorSuppressFlag(final boolean flag) {
        saveManager.saveErrorSuppressFlag(flag);
    }

    public EnumSet<DataLayer> loadDataLayers() {
        return loadManager.loadDataLayers();
    }

    public void saveDataLayers(final EnumSet<DataLayer> dataLayers) {
        saveManager.saveDataLayers(dataLayers);
    }
    public void saveDirectionOfFlowFiltersChangedFlag(final boolean changed) {
        saveManager.saveDirectionOfFlowFiltersChangedFlag(changed);
    }

    public boolean loadDirectionOfFlowTipSuppressFlag() {
        return loadManager.loadDirectionOfFlowTipSuppressFlag();
    }

    public void saveDirectionOfFlowTipSuppressFlag(final boolean flag) {
        saveManager.saveDirectionOfFlowTipSuppressFlag(flag);
    }

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

    public void saveDirectionOfFlowLastComment(final String comment) {
        saveManager.saveDirectionOfFlowLastComment(comment);
    }

    public OnewayFilter loadOnewayFilter() {
        return loadManager.loadOnewayFilter();
    }

    public void saveOnewayFilter(final OnewayFilter filter) {
        saveManager.saveOnewayFilter(filter);
    }

    public void saveMissingGeometryLastComment(final String comment) {
        saveManager.saveMissingGeoLastComment(comment);
    }

    public MissingGeometryFilter loadMissingGeometryFilter() {
        return loadManager.loadMissingGeometryFilter();
    }

    public void saveMissingGeometryFilter(final MissingGeometryFilter filter) {
        saveManager.saveMissingGeoFilter(filter);
    }

    public void saveMissingGeometryFiltersChangedFlag(final boolean changed) {
        saveManager.saveMissingGeometryFiltersChangedFlag(changed);
    }
}