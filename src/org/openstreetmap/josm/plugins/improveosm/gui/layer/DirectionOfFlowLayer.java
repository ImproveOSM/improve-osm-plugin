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
    List<RoadSegment> getItemsInsideTheBoundingBox(final Rectangle2D boundingBox) {
        return getDataSet().getItems().stream()
                .filter(roadSegment -> Util.roadSegementIsInsideBoundingBox(roadSegment, boundingBox))
                .collect(Collectors.toList());
    }

    @Override
    BasicFilterDialog getFilterDialog() {
        return new DirectionOfFlowFilterDialog();
    }

    @Override
    AbstractAction getDeleteAction() {
        return new DeleteDirectionOfFlowLayerAction();
    }


    private static class DeleteDirectionOfFlowLayerAction extends ImproveOsmDeleteLayerAction {

        private static final long serialVersionUID = -6587863325888182227L;

        @Override
        void saveLayerClosedState() {
            PreferenceManager.getInstance().saveDirectionOfFlowLayerOpenedFlag(false);
        }
    }
}