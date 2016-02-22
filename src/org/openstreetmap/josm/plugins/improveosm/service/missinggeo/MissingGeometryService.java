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

import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.josm.plugins.improveosm.argument.BoundingBox;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.SearchFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.entity.DataSet;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.service.BaseService;
import org.openstreetmap.josm.plugins.improveosm.service.Service;
import org.openstreetmap.josm.plugins.improveosm.service.ServiceException;
import org.openstreetmap.josm.plugins.improveosm.service.entity.CommentRequest;
import com.google.gson.GsonBuilder;


/**
 * Executes the MissingGeometry service methods.
 *
 * @author Beata
 * @version $Revision$
 */
public class MissingGeometryService extends BaseService implements Service<Tile> {

    @Override
    public GsonBuilder createGsonBuilder() {
        final GsonBuilder builder = super.createGsonBuilder();
        return builder;
    }

    @Override
    public DataSet<Tile> search(final BoundingBox bbox, final SearchFilter filter, final int zoom)
            throws ServiceException {
        final String url = new QueryBuilder().buildSearchQuery(bbox, (MissingGeometryFilter) filter, zoom);
        final Response response = executeGet(url, Response.class);
        verifyResponseStatus(response.getStatus());
        return new DataSet<>(response.getClusters(), response.getTiles());
    }

    @Override
    public List<Comment> retrieveComments(final Tile entity) throws ServiceException {
        final String url = new QueryBuilder().buildRetrieveCommentsQuery(entity.getX(), entity.getY());
        final Response response = executeGet(url, Response.class);
        verifyResponseStatus(response.getStatus());
        return response.getComments();
    }

    @Override
    public void comment(final Comment comment, final List<Tile> entities) throws ServiceException {
        final String url = new QueryBuilder().buildCommentQuery();
        final List<Tile> targetIds = new ArrayList<>();
        for (final Tile tile : entities) {
            targetIds.add(new Tile(tile.getX(), tile.getY()));
        }
        final CommentRequest<Tile> requestBody = new CommentRequest<>(comment, entities);
        final String content = buildRequest(requestBody, CommentRequest.class);
        final Response root = executePost(url, content, Response.class);
        verifyResponseStatus(root.getStatus());
    }
}