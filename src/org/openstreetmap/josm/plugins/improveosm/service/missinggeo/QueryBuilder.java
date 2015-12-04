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
package org.openstreetmap.josm.plugins.improveosm.service.missinggeo;

import org.openstreetmap.josm.plugins.improveosm.argument.BoundingBox;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.service.BaseQueryBuilder;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import org.openstreetmap.josm.plugins.improveosm.util.http.HttpUtil;


/**
 * Builds MissingGeometryService specific HTTP queries
 *
 * @author Beata
 * @version $Revision$
 */
final class QueryBuilder extends BaseQueryBuilder {

    String buildSearchQuery(final BoundingBox bbox, final MissingGeometryFilter filter, final int zoom) {
        final StringBuilder query = new StringBuilder();
        appendFormatFilter(query);
        appendClientFilter(query);
        appendBoundingBoxFilter(query, bbox);
        appendZoomFilter(query, zoom);

        if (filter != null) {
            appendStatusFilter(query, filter.getStatus());
            if (filter.getTypes() != null) {
                query.append(AND).append(Params.TYPE).append(EQ);
                query.append(HttpUtil.utf8Encode(filter.getTypes()));

            }
            if (filter.getCount() != null) {
                if (zoom > Config.getDirectionOfFlowInstance().getMaxClusterZoom()) {
                    query.append(AND).append(Params.NO_TRIPS);
                } else {
                    query.append(AND).append(Params.NO_POINTS);
                }
                query.append(EQ).append(filter.getCount());
            }
        }
        return build(Config.getMissingGeometryInstance().getServiceUrl(), Method.SEARCH, query);
    }

    String buildRetrieveCommentsQuery(final Integer tileX, final Integer tileY) {
        final StringBuilder query = new StringBuilder();
        appendFormatFilter(query);
        appendClientFilter(query);
        query.append(AND).append(Params.TILE_X).append(EQ).append(tileX);
        query.append(AND).append(Params.TILE_Y).append(EQ).append(tileY);
        return build(Config.getMissingGeometryInstance().getServiceUrl(), Method.RETRIEVE_COMMENTS, query);
    }

    String buildCommentQuery() {
        return build(Config.getMissingGeometryInstance().getServiceUrl(), Method.COMMENT, null);
    }

    private final class Params {

        private Params() {}

        private static final String TYPE = "type";
        private static final String NO_TRIPS = "numberOfTrips";
        private static final String NO_POINTS = "numberOfPoints";
        private static final String TILE_X = "tileX";
        private static final String TILE_Y = "tileY";
    }
}