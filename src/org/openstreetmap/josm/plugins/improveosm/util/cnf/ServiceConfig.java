/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.util.cnf;

import com.telenav.josm.common.cnf.BaseConfig;


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