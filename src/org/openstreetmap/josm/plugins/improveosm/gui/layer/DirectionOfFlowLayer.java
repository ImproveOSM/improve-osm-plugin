/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.layer;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.gui.details.common.BasicFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.gui.details.directionofflow.DirectionOfFlowFilterDialog;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.DirectionOfFlowGuiConfig;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.plugins.improveosm.util.pref.PreferenceManager;


/**
 * Defines the DirectionOfFlowLayer main functionality.
 *
 * @author Beata
 * @version $Revision$
 */
public class DirectionOfFlowLayer extends ImproveOsmLayer<RoadSegment> {

    /**
     * Builds a new Direction of flow layer.
     */
    public DirectionOfFlowLayer() {
        super(DirectionOfFlowGuiConfig.getInstance().getLayerName(), new DirectionOfFlowPaintHandler());
    }

    @Override
    public Icon getIcon() {
        return IconConfig.getInstance().getDirectionOfFlowLayerIcon();
    }

    @Override
    public String getToolTipText() {
        return DirectionOfFlowGuiConfig.getInstance().getLayerTlt();
    }

    @Override
    RoadSegment nearbyItem(final Point point) {
        return Util.nearbyRoadSegment(getDataSet().getItems(), point);
    }

    @Override
    BasicFilterDialog getFilterDialog() {
        return new DirectionOfFlowFilterDialog();
    }

    @Override
    AbstractAction getDeleteAction() {
        return new DeleteDirectionOfFlowLayerAction();
    }

    @Override
    public List<RoadSegment> getItemsInsideTheBoundingBox(final Rectangle2D boundingBox, boolean multiSelected) {
        return getDataSet().getItems().stream().filter(segment -> segment.getPoints().stream()
                .anyMatch(point -> boundingBox.contains(point.getX(), point.getY()))).collect(Collectors.toList());
    }

    private static class DeleteDirectionOfFlowLayerAction extends ImproveOsmDeleteLayerAction {

        private static final long serialVersionUID = -6587863325888182227L;

        @Override
        void saveLayerClosedState() {
            PreferenceManager.getInstance().saveDirectionOfFlowLayerOpenedFlag(false);
        }
    }
}