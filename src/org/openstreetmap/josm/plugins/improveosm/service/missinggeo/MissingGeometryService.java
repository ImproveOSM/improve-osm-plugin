/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.service.missinggeo;

import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.SearchFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.entity.DataSet;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.service.BaseService;
import org.openstreetmap.josm.plugins.improveosm.service.Service;
import org.openstreetmap.josm.plugins.improveosm.service.ServiceException;
import org.openstreetmap.josm.plugins.improveosm.service.entity.CommentRequest;
import com.telenav.josm.common.argument.BoundingBox;


/**
 * Executes the MissingGeometry service methods.
 *
 * @author Beata
 * @version $Revision$
 */
public class MissingGeometryService extends BaseService implements Service<Tile> {

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
        final CommentRequest<Tile> requestBody = new CommentRequest<>(comment, targetIds);
        final String content = buildRequest(requestBody, CommentRequest.class);
        final Response root = executePost(url, content, Response.class);
        verifyResponseStatus(root.getStatus());
    }
}