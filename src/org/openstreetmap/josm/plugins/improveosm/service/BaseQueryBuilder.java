/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.service;

import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import com.telenav.josm.common.argument.BoundingBox;
import com.telenav.josm.common.http.HttpUtil;


/**
 * Provides common functionality for building HTTP GET and POST queries.
 *
 * @author Beata
 * @version $Revision$
 */
public class BaseQueryBuilder {

    /**
     * Specifies the available service methods.
     */
    protected enum Method {
        SEARCH, COMMENT, RETRIEVE_COMMENTS {

            @Override
            public String toString() {
                return "retrieveComments";
            }
        };

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    private static final char QUESTIONM = '?';
    protected static final char EQ = '=';
    protected static final char AND = '&';


    /**
     * Appends the following parameters: version, client, format to the given query. These parameters needs to be sent
     * with any service request.
     *
     * @param query represents the HTTP query string
     * @param version the service API version
     */
    protected void appendGeneralParameters(final StringBuilder query, final String version) {
        appendFormatFilter(query);
        appendClientFilter(query);
        appendVersionFilter(query, version);
    }

    /**
     * Appends the general parameters to the given query.
     *
     * @param query represents the HTTP query string
     * @param version the service API version
     * @param bbox defines the searching area
     * @param zoom the current zoom level
     */
    protected void appendGeneralSearchFilters(final StringBuilder query, final String version, final BoundingBox bbox,
            final int zoom) {
        appendGeneralParameters(query, version);
        appendBoundingBoxFilter(query, bbox);
        appendZoomFilter(query, zoom);
    }

    /**
     * Appends the status filter to the given query. If status is null, the method does not change the query.
     *
     * @param query represents the HTTP query string
     * @param status the currently selected status
     */
    protected void appendStatusFilter(final StringBuilder query, final Status status) {
        if (status != null) {
            query.append(AND).append(Parameter.STATUS.toString()).append(EQ).append(HttpUtil.utf8Encode(status.name()));
        }
    }

    /**
     * Builds the final HTTP query string.
     *
     * @param serviceUrl represents the service URL
     * @param method represents the service method to be called
     * @param query represents the HTTP query string
     * @return a {@code String} object
     */
    protected String build(final String serviceUrl, final Method method, final StringBuilder query) {
        final StringBuilder url = new StringBuilder(serviceUrl);
        url.append(method.toString());
        if (query != null) {
            url.append(QUESTIONM).append(query);
        }
        return url.toString();
    }

    private void appendBoundingBoxFilter(final StringBuilder query, final BoundingBox bbox) {
        query.append(AND).append(Parameter.NORTH.toString()).append(EQ).append(bbox.getNorth());
        query.append(AND).append(Parameter.SOUTH.toString()).append(EQ).append(bbox.getSouth());
        query.append(AND).append(Parameter.EAST.toString()).append(EQ).append(bbox.getEast());
        query.append(AND).append(Parameter.WEST.toString()).append(EQ).append(bbox.getWest());
    }

    private void appendFormatFilter(final StringBuilder query) {
        query.append(Parameter.FORMAT.toString()).append(EQ).append(Parameter.JOSN.toString());
    }

    private void appendZoomFilter(final StringBuilder query, final int zoom) {
        query.append(AND).append(Parameter.ZOOM.toString()).append(EQ).append(zoom);
    }

    private void appendClientFilter(final StringBuilder query) {
        query.append(AND).append(Parameter.CLIENT.toString()).append(EQ).append(Parameter.JOSM.toString());
    }

    private void appendVersionFilter(final StringBuilder query, final String version) {
        query.append(AND).append(Parameter.VERSION.toString()).append(EQ).append(HttpUtil.utf8Encode(version));
    }

    /* commonly used parameters */
    private enum Parameter {
        NORTH, SOUTH, EAST, WEST, ZOOM, STATUS, FORMAT, JOSN, CLIENT, JOSM, VERSION;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}