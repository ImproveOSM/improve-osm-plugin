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

    private static final Config DIRECTION_OF_FLOW_INSTANCE = new Config("improveosm_dof_config.properties");
    private static final Config MISSING_GEOMETRY_INSTANCE = new Config("improveosm_mg_config.properties");
    private static final Config TURN_RESTRICTION_INSTANCE = new Config("improveosm_tr_config.properties");

    private static final int MAX_CLUSTER_ZOOM = 14;

    private final String serviceUrl;
    private int maxClusterZoom;
    private final String version;


    private Config(final String fileName) {
        super(fileName);
        serviceUrl = readProperty("service.url");
        if (serviceUrl == null) {
            throw new ExceptionInInitializerError("Missing service url.");
        }

        try {
            maxClusterZoom = Integer.parseInt(readProperty("zoom.cluster.max"));
        } catch (final NumberFormatException e) {
            maxClusterZoom = MAX_CLUSTER_ZOOM;
        }
        version = readProperty("version");
    }


    public static Config getDirectionOfFlowInstance() {
        return DIRECTION_OF_FLOW_INSTANCE;
    }

    public static Config getMissingGeometryInstance() {
        return MISSING_GEOMETRY_INSTANCE;
    }

    public static Config getTurnRestrictionInstance() {
        return TURN_RESTRICTION_INSTANCE;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public int getMaxClusterZoom() {
        return maxClusterZoom;
    }

    public String getVersion() {
        return version;
    }
}