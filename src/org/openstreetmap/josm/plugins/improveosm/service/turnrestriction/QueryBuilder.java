/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.service.turnrestriction;

import org.openstreetmap.josm.plugins.improveosm.argument.TurnRestrictionFilter;
import org.openstreetmap.josm.plugins.improveosm.service.BaseQueryBuilder;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.ServiceConfig;
import com.telenav.josm.common.argument.BoundingBox;
import com.telenav.josm.common.http.HttpUtil;


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

        private static final String CONFIDENCE_LEVEL = "confidenceLevel";
        private static final String TARGET_ID = "targetId";

        private Parameter() {}

    }
}