/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
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
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import com.telenav.josm.common.entity.Coordinate;
import com.telenav.josm.common.entity.Pair;
import com.telenav.josm.common.gui.PaintManager;
import com.telenav.josm.common.util.GeometryUtil;


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
                getArrowGeometry(mapView, segment.getPoints(), arrowLength);
        final List<Point> geometry = PaintUtil.toPoints(mapView, segment.getPoints());
        PaintManager.drawDirectedSegment(graphics, geometry, arrowGeometry, color, stroke);
    }

    private Pair<Pair<Point, Point>, Pair<Point, Point>> getArrowGeometry(final MapView mapView,
            final List<LatLon> geometry, final double length) {
        final double bearing =
                Math.toDegrees(geometry.get(geometry.size() - 1).bearing(geometry.get(geometry.size() - 2)));
        final Pair<Coordinate, Coordinate> arrowEndCoordinates = GeometryUtil.arrowEndPoints(
                new Coordinate(geometry.get(geometry.size() - 1).lat(), geometry.get(geometry.size() - 1).lon()),
                bearing, length);
        final Pair<Point, Point> arrowLine1 =
                new Pair<>(mapView.getPoint(geometry.get(geometry.size() - 1)), mapView.getPoint(
                        new LatLon(arrowEndCoordinates.getFirst().getLat(), arrowEndCoordinates.getFirst().getLon())));
        final Pair<Point, Point> arrowLine2 = new Pair<>(mapView.getPoint(geometry.get(geometry.size() - 1)),
                mapView.getPoint(new LatLon(arrowEndCoordinates.getSecond().getLat(),
                        arrowEndCoordinates.getSecond().getLon())));
        return new Pair<>(arrowLine1, arrowLine2);
    }

    @Override
    Color getClusterColor() {
        return DIRECTIONOFFLOW_CLUSTER_COLOR;
    }
}