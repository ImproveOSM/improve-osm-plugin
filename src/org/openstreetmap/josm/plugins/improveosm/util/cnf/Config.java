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


    private int maxClusterZoom;
    private int commentMaxLength;
    private int commentDisplayLength;
    private final String feedbackUrl;

    private final EnumSet<DataLayer> enabledDataLayers;
    private final EnumSet<LocationPref> enabledLocationPref;

    private final String[] locationUrlPatterns;
    private final String[] locationUrlVariablePart;
    private final String locationUrlOpenStreetView;


    private Config() {
        super(CONFIG_FILE);
        feedbackUrl = readProperty("feedback.url");

        try {
            maxClusterZoom = Integer.parseInt(readProperty("zoom.cluster.max"));
        } catch (final NumberFormatException e) {
            maxClusterZoom = MAX_CLUSTER_ZOOM;
        }
        try {
            commentMaxLength = Integer.parseInt(readProperty("comment.max.length"));
        } catch (final NumberFormatException e) {
            commentMaxLength = MAX_COMMENT_LENGTH;
        }

        try {
            commentDisplayLength = Integer.parseInt(readProperty("comment.display.max.length"));
        } catch (final NumberFormatException e) {
            commentDisplayLength = MAX_COMMENT_DISPLAY_LENGTH;
        }

        final String[] enabledDataLayersValues = readPropertiesArray("layers.enabled");
        if (enabledDataLayersValues != null) {
            enabledDataLayers = EnumSet.noneOf(DataLayer.class);
            for (final String value : enabledDataLayersValues) {
                enabledDataLayers.add(DataLayer.valueOf(value));
            }
        } else {
            enabledDataLayers = EnumSet.allOf(DataLayer.class);
        }

        final String[] enabledLocationSettingsValues = readPropertiesArray("locationPref.enabled");
        if (enabledLocationSettingsValues != null) {
            enabledLocationPref = EnumSet.noneOf(LocationPref.class);
            for (final String value : enabledLocationSettingsValues) {
                enabledLocationPref.add(LocationPref.valueOf(value));
            }
        } else {
            enabledLocationPref = EnumSet.allOf(LocationPref.class);
        }

        locationUrlPatterns = readPropertiesArray("locationPref.patterns");
        locationUrlVariablePart = readPropertiesArray("locationPref.url.variablePart");
        locationUrlOpenStreetView = readProperty("locationPref.url.openstreetview");
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

    public EnumSet<DataLayer> getEnabledDataLayers() {
        return enabledDataLayers;
    }

    public EnumSet<LocationPref> getEnabledLocationPref() {
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