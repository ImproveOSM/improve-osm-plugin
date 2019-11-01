/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.service.turnrestriction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.improveosm.argument.SearchFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.TurnRestrictionFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.entity.DataSet;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.service.BaseService;
import org.openstreetmap.josm.plugins.improveosm.service.Service;
import org.openstreetmap.josm.plugins.improveosm.service.ServiceException;
import org.openstreetmap.josm.plugins.improveosm.service.entity.CommentRequest;
import com.telenav.josm.common.argument.BoundingBox;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public class TurnRestrictionService extends BaseService implements Service<TurnRestriction> {

    @Override
    public DataSet<TurnRestriction> search(final BoundingBox bbox, final SearchFilter filter, final int zoom)
            throws ServiceException {
        final String url = new QueryBuilder().buildSearchQuery(bbox, (TurnRestrictionFilter) filter, zoom);
        final Response response = executeGet(url, Response.class);
        verifyResponseStatus(response.getStatus());
        final Map<LatLon, TurnRestriction> map = buildTurnRestrictionMap(response.getEntities());
        return new DataSet<>(response.getClusters(), new ArrayList<>(map.values()));
    }

    private Map<LatLon, TurnRestriction> buildTurnRestrictionMap(final List<TurnRestriction> entities) {
        final Map<LatLon, TurnRestriction> map = new HashMap<>();
        if (entities != null) {
            for (final TurnRestriction elem : entities) {
                if (map.containsKey(elem.getPoint())) {
                    // rebuild TurnRestriction
                    final TurnRestriction mapElem = map.get(elem.getPoint());
                    if (mapElem.getTurnRestrictions() == null) {
                        final List<TurnRestriction> turnRestrictions = new ArrayList<>();
                        turnRestrictions.add(new TurnRestriction(mapElem.getId(), mapElem.getSegments(),
                                mapElem.getPoint(), mapElem.getStatus(), mapElem.getTurnType(),
                                mapElem.getConfidenceLevel(), mapElem.getNumberOfPasses()));
                        turnRestrictions.add(
                                new TurnRestriction(elem.getId(), elem.getSegments(), elem.getPoint(), elem.getStatus(),
                                        elem.getTurnType(), elem.getConfidenceLevel(), elem.getNumberOfPasses()));
                        map.put(elem.getPoint(), new TurnRestriction(elem.getPoint(), turnRestrictions));
                    } else {
                        mapElem.getTurnRestrictions()
                        .add(new TurnRestriction(elem.getId(), elem.getSegments(), elem.getPoint(),
                                elem.getStatus(), elem.getTurnType(), elem.getConfidenceLevel(),
                                elem.getNumberOfPasses()));
                    }
                } else {
                    map.put(elem.getPoint(), elem);
                }
            }
        }
        return map;
    }

    @Override
    public List<Comment> retrieveComments(final TurnRestriction entity) throws ServiceException {
        final String url = new QueryBuilder().buildRetrieveCommentsQuery(entity.getId());
        final Response response = executeGet(url, Response.class);
        verifyResponseStatus(response.getStatus());
        return response.getComments();
    }

    @Override
    public void comment(final Comment comment, final List<TurnRestriction> entities) throws ServiceException {
        final List<String> targetIds = new ArrayList<>();
        for (final TurnRestriction turnRestriction : entities) {
            targetIds.add(turnRestriction.getId());
        }
        final CommentRequest<String> requestBody = new CommentRequest<>(comment, targetIds);
        final String content = buildRequest(requestBody, CommentRequest.class);
        final String url = new QueryBuilder().buildCommentQuery();
        final Response root = executePost(url, content, Response.class);
        verifyResponseStatus(root.getStatus());
    }
}