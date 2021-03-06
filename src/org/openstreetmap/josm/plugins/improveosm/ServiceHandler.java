/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.plugins.improveosm.argument.SearchFilter;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.entity.DataLayer;
import org.openstreetmap.josm.plugins.improveosm.entity.DataSet;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.service.Service;
import org.openstreetmap.josm.plugins.improveosm.service.ServiceException;
import org.openstreetmap.josm.plugins.improveosm.service.directioofflow.DirectionOfFlowService;
import org.openstreetmap.josm.plugins.improveosm.service.missinggeo.MissingGeometryService;
import org.openstreetmap.josm.plugins.improveosm.service.turnrestriction.TurnRestrictionService;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;
import com.telenav.josm.common.argument.BoundingBox;


/**
 * Executes the service operations corresponding to user actions. If an operation fails, a corresponding message is
 * displayed to the user.
 *
 * @author Beata
 * @version $Revision$
 * @param <T>
 */
public final class ServiceHandler<T> {

    private static final ServiceHandler<Tile> MISSING_GEOMETRY_HANDLER =
            new ServiceHandler<>(DataLayer.MISSING_GEOMETRY);
    private static final ServiceHandler<RoadSegment> DIRECTION_OF_FLOW_HANDLER =
            new ServiceHandler<>(DataLayer.DIRECTION_OF_FLOW);
    private static final ServiceHandler<TurnRestriction> TURN_RESTRICTION_HANDLER =
            new ServiceHandler<>(DataLayer.TURN_RESTRICTION);

    private final Service<T> service;


    @SuppressWarnings("unchecked")
    private ServiceHandler(final DataLayer layerType) {
        switch (layerType) {
            case MISSING_GEOMETRY:
                service = (Service<T>) new MissingGeometryService();
                break;
            case DIRECTION_OF_FLOW:
                service = (Service<T>) new DirectionOfFlowService();
                break;
            default:
                // turn restriction service
                service = (Service<T>) new TurnRestrictionService();
                break;
        }
    }


    public static ServiceHandler<Tile> getMissingGeometryHandler() {
        return MISSING_GEOMETRY_HANDLER;
    }

    public static ServiceHandler<RoadSegment> getDirectionOfFlowHandler() {
        return DIRECTION_OF_FLOW_HANDLER;
    }

    public static ServiceHandler<TurnRestriction> getTurnRestrictionHandler() {
        return TURN_RESTRICTION_HANDLER;
    }

    public DataSet<T> search(final BoundingBox bbox, final SearchFilter filter, final int zoom) {
        DataSet<T> result = new DataSet<>();
        try {
            result = service.search(bbox, filter, zoom);
        } catch (final ServiceException e) {
            handleException(e, true);
        }
        return result;
    }

    public List<Comment> retrieveComments(final T entity) {
        List<Comment> comments = new ArrayList<>();
        try {
            comments = service.retrieveComments(entity);
        } catch (final ServiceException e) {
            handleException(e, false);
        }
        return comments;
    }

    public void comment(final Comment comment, final List<T> entities) {
        try {
            service.comment(comment, entities);
        } catch (final ServiceException e) {
            handleException(e, false);
        }
    }

    private void handleException(final Exception e, final boolean suppress) {
        if (suppress) {
            if (!PreferenceManager.getInstance().loadErrorSuppressFlag()) {
                PreferenceManager.getInstance().saveErrorSuppressFlag(suppress);
                JOptionPane.showMessageDialog(MainApplication.getMainFrame(), e.getMessage(), "Operation failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(MainApplication.getMainFrame(), e.getMessage(), "Operation failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}