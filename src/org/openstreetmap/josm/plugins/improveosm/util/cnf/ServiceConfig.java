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
 * Holds back-end service connection run-time configuration.
 *
 * @author Beata
 * @version $Revision$
 */
public final class ServiceConfig extends BaseConfig {

    private static final ServiceConfig DIRECTION_OF_FLOW_INSTANCE =
            new ServiceConfig("improveosm_dof_config.properties");
    private static final ServiceConfig MISSING_GEOMETRY_INSTANCE = new ServiceConfig("improveosm_mg_config.properties");
    private static final ServiceConfig TURN_RESTRICTION_INSTANCE = new ServiceConfig("improveosm_tr_config.properties");


    private final String serviceUrl;
    private final String version;


    private ServiceConfig(final String fileName) {
        super(fileName);
        serviceUrl = readProperty("service.url");
        if (serviceUrl == null) {
            throw new ExceptionInInitializerError("Missing service url.");
        }
        version = readProperty("version");
    }


    public static ServiceConfig getDirectionOfFlowInstance() {
        return DIRECTION_OF_FLOW_INSTANCE;
    }

    public static ServiceConfig getMissingGeometryInstance() {
        return MISSING_GEOMETRY_INSTANCE;
    }

    public static ServiceConfig getTurnRestrictionInstance() {
        return TURN_RESTRICTION_INSTANCE;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public String getVersion() {
        return version;
    }
}