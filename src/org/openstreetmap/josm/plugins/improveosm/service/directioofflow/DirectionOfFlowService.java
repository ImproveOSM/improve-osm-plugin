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
package org.openstreetmap.josm.plugins.improveosm.service.directioofflow;

import java.util.List;
import org.openstreetmap.josm.plugins.improveosm.argument.BoundingBox;
import org.openstreetmap.josm.plugins.improveosm.argument.OnewayFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.SearchFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.entity.DataSet;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.service.BaseService;
import org.openstreetmap.josm.plugins.improveosm.service.Service;
import org.openstreetmap.josm.plugins.improveosm.service.ServiceException;
import org.openstreetmap.josm.plugins.improveosm.service.directioofflow.entity.Request;
import org.openstreetmap.josm.plugins.improveosm.service.directioofflow.entity.Response;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public class DirectionOfFlowService extends BaseService implements Service<RoadSegment> {

    @Override
    public DataSet<RoadSegment> search(final BoundingBox bbox, final SearchFilter filter, final int zoom)
            throws ServiceException {
        final String url = new QueryBuilder().buildSearchQuery(bbox, (OnewayFilter) filter, zoom);
        final Response response = executeGet(url, Response.class);
        verifyResponseStatus(response.getStatus());
        return new DataSet<>(response.getClusters(), response.getRoadSegments());
    }

    @Override
    public List<Comment> retrieveComments(final RoadSegment entity) throws ServiceException {
        final String url = new QueryBuilder().buildRetrieveCommentsQuery(entity.getWayId(), entity.getFromNodeId(),
                entity.getToNodeId());
        final Response response = executeGet(url, Response.class);
        verifyResponseStatus(response.getStatus());
        return response.getComments();
    }

    @Override
    public void comment(final Comment comment, final List<RoadSegment> entities) throws ServiceException {
        final String url = new QueryBuilder().buildCommentQuery();
        final Request commentRequest = new Request(comment, entities);
        final String content = buildRequest(commentRequest, Request.class);
        final Response root = executePost(url, content, Response.class);
        verifyResponseStatus(root.getStatus());
    }

}