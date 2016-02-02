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
package org.openstreetmap.josm.plugins.improveosm.service;

import org.openstreetmap.josm.plugins.improveosm.argument.BoundingBox;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.util.http.HttpUtil;


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
    public enum Method {
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

    /* commonly used parameters */
    private enum Parameter {
        NORTH, SOUTH, EAST, WEST, ZOOM, STATUS, FORMAT, JOSN, CLIENT, JOSM, VERSION;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    private static final char QUESTIONM = '?';
    public static final char EQ = '=';
    public static final char AND = '&';

    public void appendGeneralParameters(final StringBuilder query, final String version) {
        appendFormatFilter(query);
        appendClientFilter(query);
        appendVersionFilter(query, version);
    }

    public void appendGeneralSearchFilters(final StringBuilder query, final String version, final BoundingBox bbox,
            final int zoom) {
        appendGeneralParameters(query, version);
        appendBoundingBoxFilter(query, bbox);
        appendZoomFilter(query, zoom);
    }

    /**
     * Appends the bounding box filter to the given query.
     *
     * @param query represents the HTTP query string
     * @param bbox a {@code BoundingBox} object
     */
    private void appendBoundingBoxFilter(final StringBuilder query, final BoundingBox bbox) {
        query.append(AND).append(Parameter.NORTH.toString()).append(EQ).append(bbox.getNorth());
        query.append(AND).append(Parameter.SOUTH.toString()).append(EQ).append(bbox.getSouth());
        query.append(AND).append(Parameter.EAST.toString()).append(EQ).append(bbox.getEast());
        query.append(AND).append(Parameter.WEST.toString()).append(EQ).append(bbox.getWest());
    }

    /**
     * Appends the format filter and it's value to the given query. This filter needs to be added for each service
     * method call.
     *
     * @param query represents the HTTP query string
     */
    public void appendFormatFilter(final StringBuilder query) {
        query.append(Parameter.FORMAT.toString()).append(EQ).append(Parameter.JOSN.toString());
    }

    /**
     * Appends the status filter to the given query. If status is null, the method does not change the query.
     *
     * @param query represents the HTTP query string
     * @param status the currently selected status
     */
    public void appendStatusFilter(final StringBuilder query, final Status status) {
        if (status != null) {
            query.append(AND).append(Parameter.STATUS.toString()).append(EQ).append(HttpUtil.utf8Encode(status.name()));
        }
    }

    /**
     * Appends the zoom filter to the given query.
     *
     * @param query represents the HTTP query string
     * @param zoom represents the current zoom level.
     */
    private void appendZoomFilter(final StringBuilder query, final int zoom) {
        query.append(AND).append(Parameter.ZOOM.toString()).append(EQ).append(zoom);
    }

    /**
     * Appends the client filter to the given query. This filter needs to be added for each service method call.
     *
     * @param query represents the HTTP query string
     */
    public void appendClientFilter(final StringBuilder query) {
        query.append(AND).append(Parameter.CLIENT.toString()).append(EQ).append(Parameter.JOSM.toString());
    }

    public void appendVersionFilter(final StringBuilder query, final String version) {
        query.append(AND).append(Parameter.VERSION.toString()).append(EQ).append(HttpUtil.utf8Encode(version));
    }

    /**
     * Builds the final HTTP query string.
     *
     * @param serviceUrl represents the service URL
     * @param method represents the service method to be called
     * @param query represents the HTTP query string
     * @return a {@code String} object
     */
    public String build(final String serviceUrl, final Method method, final StringBuilder query) {
        final StringBuilder url = new StringBuilder(serviceUrl);
        url.append(method.toString());
        if (query != null) {
            url.append(QUESTIONM).append(query);
        }
        return url.toString();
    }
}