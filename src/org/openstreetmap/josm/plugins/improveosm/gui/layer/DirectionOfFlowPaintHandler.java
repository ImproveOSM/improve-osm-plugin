/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.layer;

import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ARROW_LENGTH;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.DIRECTIONOFFLOW_CLUSTER_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ROAD_SEGMENT_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ROAD_SEGMENT_SEL_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ROAD_SEGMENT_SEL_STROKE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ROAD_SEGMENT_STROKE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.SEL_ARROW_LENGTH;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.List;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import com.telenav.josm.common.entity.Pair;
import com.telenav.josm.common.gui.PaintManager;


/**
 *
 * @author beataj
 * @version $Revision$
 */
final class DirectionOfFlowPaintHandler extends PaintHandler<RoadSegment> {


    @Override
    void drawItem(final Graphics2D graphics, final MapView mapView, final RoadSegment segment,
            final boolean isSelected) {
        Stroke stroke;
        Color color;
        // draw segment
        if (isSelected) {
            stroke = ROAD_SEGMENT_SEL_STROKE;
            color = ROAD_SEGMENT_SEL_COLOR;
        } else {
            stroke = ROAD_SEGMENT_STROKE;
            color = ROAD_SEGMENT_COLOR;
        }
        double arrowLength = isSelected ? SEL_ARROW_LENGTH : ARROW_LENGTH;
        arrowLength = PaintUtil.arrowLength(mapView, arrowLength);
        final Pair<Pair<Point, Point>, Pair<Point, Point>> arrowGeometry =
                PaintUtil.arrowGeometry(mapView, segment.getPoints(), false, arrowLength);
        final List<Point> geometry = PaintUtil.toPoints(mapView, segment.getPoints());
        PaintManager.drawDirectedSegment(graphics, geometry, arrowGeometry, color, stroke);
    }

    @Override
    Color getClusterColor() {
        return DIRECTIONOFFLOW_CLUSTER_COLOR;
    }
}