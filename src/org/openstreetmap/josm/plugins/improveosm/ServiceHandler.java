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
package org.openstreetmap.josm.plugins.improveosm;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.improveosm.argument.BoundingBox;
import org.openstreetmap.josm.plugins.improveosm.argument.MissingGeometryFilter;
import org.openstreetmap.josm.plugins.improveosm.argument.OnewayFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.entity.DataSet;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.service.ServiceException;
import org.openstreetmap.josm.plugins.improveosm.service.directioofflow.DirectionOfFlowService;
import org.openstreetmap.josm.plugins.improveosm.service.missinggeo.MissingGeometryService;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 * Executes the service operations corresponding to user actions. If an operation fails, a corresponding message is
 * displayed to the user.
 *
 * @author Beata
 * @version $Revision$
 */
class ServiceHandler {

    private final MissingGeometryService missingGeometryService = new MissingGeometryService();
    private final DirectionOfFlowService directionOfFlowService = new DirectionOfFlowService();

    private static final ServiceHandler INSTANCE = new ServiceHandler();

    static ServiceHandler getInstance() {
        return INSTANCE;
    }


    DataSet<Tile> searchMissingGeometryData(final BoundingBox bbox, final int zoom) {
        DataSet<Tile> result = new DataSet<>();
        try {
            final MissingGeometryFilter filter = PreferenceManager.getInstance().loadMissingGeometryFilter();
            result = missingGeometryService.search(bbox, filter, zoom);
        } catch (final ServiceException e) {
            handleException(e, true);
        }
        return result;
    }

    DataSet<RoadSegment> searchDirectionOfFlowData(final BoundingBox bbox, final int zoom) {
        DataSet<RoadSegment> result = new DataSet<>();
        try {
            final OnewayFilter filter = PreferenceManager.getInstance().loadOnewayFilter();
            result = directionOfFlowService.search(bbox, filter, zoom);
        } catch (final ServiceException e) {
            handleException(e, true);
        }
        return result;
    }

    List<Comment> retrieveComments(final Tile tile) {
        List<Comment> comments = new ArrayList<>();
        try {
            comments = missingGeometryService.retrieveComments(tile);
        } catch (final ServiceException e) {
            handleException(e, false);
        }
        return comments;
    }

    List<Comment> retrieveComments(final RoadSegment roadSegment) {
        List<Comment> comments = new ArrayList<>();
        try {
            comments = directionOfFlowService.retrieveComments(roadSegment);
        } catch (final ServiceException e) {
            handleException(e, false);
        }
        return comments;
    }

    void commentTiles(final Comment comment, final List<Tile> tiles) {
        try {
            missingGeometryService.comment(comment, tiles);
        } catch (final ServiceException e) {
            handleException(e, false);
        }
    }

    void commentRoadSegments(final Comment comment, final List<RoadSegment> roadSegments) {
        try {
            directionOfFlowService.comment(comment, roadSegments);
        } catch (final ServiceException e) {
            handleException(e, false);
        }
    }

    private void handleException(final Exception e, final boolean suppress) {
        if (suppress) {
            if (!PreferenceManager.getInstance().loadErrorSuppressFlag()) {
                PreferenceManager.getInstance().saveErrorSuppressFlag(suppress);
                JOptionPane.showMessageDialog(Main.parent, e.getMessage(), "Operation failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(Main.parent, e.getMessage(), "Operation failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}