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

        final String enabledDataLayersValue = readProperty("layers.enabled");
        if (enabledDataLayersValue != null && !enabledDataLayersValue.isEmpty()) {
            enabledDataLayers = EnumSet.noneOf(DataLayer.class);
            for (final String value : enabledDataLayersValue.split(SEPARATOR)) {
                enabledDataLayers.add(DataLayer.valueOf(value));
            }
        } else {
            enabledDataLayers = EnumSet.allOf(DataLayer.class);
        }
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
}