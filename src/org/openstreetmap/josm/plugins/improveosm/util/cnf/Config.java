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
package org.openstreetmap.josm.plugins.improveosm.util.cnf;

import java.util.EnumSet;
import java.util.Set;
import org.openstreetmap.josm.plugins.improveosm.entity.DataLayer;
import org.openstreetmap.josm.plugins.improveosm.entity.LocationPref;
import com.telenav.josm.common.cnf.BaseConfig;


/**
 * Holds commonly used run time properties.
 *
 * @author Beata
 * @version $Revision$
 */
public final class Config extends BaseConfig {

    private static final Config INSTANCE = new Config();

    private static final String CONFIG_FILE = "improveosm_config.properties";

    private static final int MAX_CLUSTER_ZOOM = 14;
    private static final int MAX_COMMENT_LENGTH = 500;
    private static final int MAX_COMMENT_DISPLAY_LENGTH = 150;


    private final int maxClusterZoom;
    private final int commentMaxLength;
    private final int commentDisplayLength;
    private final String feedbackUrl;

    private final EnumSet<DataLayer> enabledDataLayers;
    private final EnumSet<LocationPref> enabledLocationPref;

    private final String[] locationUrlPatterns;
    private final String[] locationUrlVariablePart;
    private final String locationUrlOpenStreetView;


    private Config() {
        super(CONFIG_FILE);
        feedbackUrl = readProperty("feedback.url");
        maxClusterZoom = readIntegerProperty(readProperty("zoom.cluster.max"), MAX_CLUSTER_ZOOM);
        commentMaxLength = readIntegerProperty(readProperty("comment.max.length"), MAX_COMMENT_LENGTH);
        commentDisplayLength =
                readIntegerProperty(readProperty("comment.display.max.length"), MAX_COMMENT_DISPLAY_LENGTH);
        enabledDataLayers = loadEnabledDataLayers();
        enabledLocationPref = loadEnabledLocationPref();
        locationUrlPatterns = readPropertiesArray("locationPref.patterns");
        locationUrlVariablePart = readPropertiesArray("locationPref.url.variablePart");
        locationUrlOpenStreetView = readProperty("locationPref.url.openstreetview");
    }

    private EnumSet<DataLayer> loadEnabledDataLayers() {
        EnumSet<DataLayer> result;
        final String[] enabledDataLayersValues = readPropertiesArray("layers.enabled");
        if (enabledDataLayersValues != null) {
            result = EnumSet.noneOf(DataLayer.class);
            for (final String value : enabledDataLayersValues) {
                final DataLayer dataLayer = DataLayer.getDataLayer(value);
                if (dataLayer != null) {
                    result.add(dataLayer);
                }
            }
            if (result.isEmpty()) {
                result = EnumSet.allOf(DataLayer.class);
            }
        } else {
            result = EnumSet.allOf(DataLayer.class);
        }
        return result;
    }

    private EnumSet<LocationPref> loadEnabledLocationPref() {
        EnumSet<LocationPref> result;
        final String[] enabledLocationSettingsValues = readPropertiesArray("locationPref.enabled");
        if (enabledLocationSettingsValues != null) {
            result = EnumSet.noneOf(LocationPref.class);
            for (final String value : enabledLocationSettingsValues) {
                result.add(LocationPref.valueOf(value));
            }
        } else {
            result = EnumSet.allOf(LocationPref.class);
        }
        return result;
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    public int getMaxClusterZoom() {
        return maxClusterZoom;
    }

    public int getCommentMaxLength() {
        return commentMaxLength;
    }

    public int getCommentDisplayLength() {
        return commentDisplayLength;
    }

    public String getFeedbackUrl() {
        return feedbackUrl;
    }

    public Set<DataLayer> getEnabledDataLayers() {
        return enabledDataLayers;
    }

    public Set<LocationPref> getEnabledLocationPref() {
        return enabledLocationPref;
    }

    public String[] getLocationPrefUrlPatterns() {
        return locationUrlPatterns;
    }

    public String[] getLocationPrefUrlVariablePart() {
        return locationUrlVariablePart;
    }

    public String getLocationPrefOpenStreetView() {
        return locationUrlOpenStreetView;
    }
}