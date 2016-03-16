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
package org.openstreetmap.josm.plugins.improveosm.service.turnrestriction;

import org.openstreetmap.josm.plugins.improveosm.argument.BoundingBox;
import org.openstreetmap.josm.plugins.improveosm.argument.TurnRestrictionFilter;
import org.openstreetmap.josm.plugins.improveosm.service.BaseQueryBuilder;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.ServiceConfig;
import org.openstreetmap.josm.plugins.improveosm.util.http.HttpUtil;


/**
 *
 * @author Beata
 * @version $Revision$
 */
final class QueryBuilder extends BaseQueryBuilder {

    String buildSearchQuery(final BoundingBox bbox, final TurnRestrictionFilter filter, final int zoom) {
        final StringBuilder query = new StringBuilder();
        appendGeneralSearchFilters(query, ServiceConfig.getTurnRestrictionInstance().getVersion(), bbox, zoom);

        if (filter != null) {
            appendStatusFilter(query, filter.getStatus());
            if (filter.getConfidenceLevels() != null) {
                query.append(AND).append(Parameter.CONFIDENCE_LEVEL).append(EQ);
                query.append(HttpUtil.utf8Encode(filter.getConfidenceLevels()));
            }
        }
        return build(ServiceConfig.getTurnRestrictionInstance().getServiceUrl(), Method.SEARCH, query);
    }

    String buildRetrieveCommentsQuery(final String targetId) {
        final StringBuilder query = new StringBuilder();
        appendGeneralParameters(query, ServiceConfig.getTurnRestrictionInstance().getVersion());
        query.append(AND).append(Parameter.TARGET_ID).append(EQ).append(HttpUtil.utf8Encode(targetId));
        return build(ServiceConfig.getTurnRestrictionInstance().getServiceUrl(), Method.RETRIEVE_COMMENTS, query);
    }

    String buildCommentQuery() {
        final StringBuilder query = new StringBuilder();
        appendGeneralParameters(query, ServiceConfig.getTurnRestrictionInstance().getVersion());
        return build(ServiceConfig.getTurnRestrictionInstance().getServiceUrl(), Method.COMMENT, query);
    }


    private final class Parameter {

        private Parameter() {}

        private static final String CONFIDENCE_LEVEL = "confidenceLevel";
        private static final String TARGET_ID = "targetId";
    }
}