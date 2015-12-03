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

/**
 * Holds run-time configuration.
 *
 * @author Beata
 * @version $Revision$
 */
public final class Config extends BaseConfig {

    private static final Config DIRECTION_OF_FLOW_INSTANCE = new Config("directionofflow_config.properties");
    private static final Config MISSING_GEO_INSTANCE = new Config("missinggeo_config.properties");
    private static final int MAX_CLUSTER_ZOOM = 14;

    private final String serviceUrl;
    private final String feedbackUrl;
    private int maxClusterZoom;

    private Config(final String fileName) {
        super(fileName);
        serviceUrl = readProperty("service.url");
        if (serviceUrl == null) {
            throw new ExceptionInInitializerError("Missing service url.");
        }

        feedbackUrl = readProperty("feedback.url");

        try {
            maxClusterZoom = Integer.parseInt(readProperty("zoom.cluster.max"));
        } catch (final NumberFormatException e) {
            maxClusterZoom = MAX_CLUSTER_ZOOM;
        }
    }


    public static Config getDirectionOfFlowInstance() {
        return DIRECTION_OF_FLOW_INSTANCE;
    }

    public static Config getMissingGeoInstance() {
        return MISSING_GEO_INSTANCE;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public String getFeedbackUrl() {
        return feedbackUrl;
    }

    public int getMaxClusterZoom() {
        return maxClusterZoom;
    }
}