/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.layer;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import com.telenav.josm.common.entity.Coordinate;
import com.telenav.josm.common.entity.Pair;
import com.telenav.josm.common.util.GeometryUtil;


/**
 * Holds utility methods used by drawing methods.
 *
 * @author beataj
 * @version $Revision$
 */
final class PaintUtil {

    private static final int ZOOM_15 = 15;
    private static final int ZOOM_16 = 16;
    private static final int ZOOM_17 = 17;
    private static final int ZOOM_18 = 18;
    private static final int ZOOM_15_MULTIPLY = 8;
    private static final int ZOOM_16_MULTIPLY = 6;
    private static final int ZOOM_17_MULTIPLY = 4;
    private static final double ZOOM_18_MULTIPLY = 2;


    private PaintUtil() {}

    /**
     * Returns the length of an arrow based on the current zoom level
     *
     * @param mapView a {@code MapView} used for computing the current zoom level
     * @param initialLength the arrow's initial length
     * @return the arrow length
     */
    static double arrowLength(final MapView mapView, final double initialLength) {
        double arrowLength;
        final int zoom = Util.zoom(mapView.getRealBounds());
        switch (zoom) {
            case ZOOM_15:
                arrowLength = initialLength * ZOOM_15_MULTIPLY;
                break;
            case ZOOM_16:
                arrowLength = initialLength * ZOOM_16_MULTIPLY;
                break;
            case ZOOM_17:
                arrowLength = initialLength * ZOOM_17_MULTIPLY;
                break;
            case ZOOM_18:
                arrowLength = initialLength * ZOOM_18_MULTIPLY;
                break;
            default:
                // no change
                arrowLength = initialLength;
                break;
        }
        return arrowLength;
    }

    /**
     * Transforms geographic coordinates into screen points.
     *
     * @param mapView a {@code MapView} used for coordinate transformation
     * @param geometry a list of {@code LatLon} representing the geographic coordinates
     * @return a list of {@code Point}s
     */
    static List<Point> toPoints(final MapView mapView, final List<LatLon> geometry) {
        final List<Point> points = new ArrayList<>();
        for (final LatLon latLon : geometry) {
            points.add(mapView.getPoint(latLon));
        }
        return points;
    }

    static Pair<Pair<Point, Point>, Pair<Point, Point>> arrowGeometry(final MapView mapView, final List<LatLon> points,
            final boolean isFromSegment, final double length) {
        LatLon arrowPoint;
        double bearing;
        if (isFromSegment) {
            arrowPoint = arrowPoint(points);
            bearing = Math.toDegrees(points.get(points.size() - 1).bearing(arrowPoint));
        } else {
            arrowPoint = points.get(points.size() - 1);
            bearing = Math.toDegrees(points.get(points.size() - 1).bearing(points.get(points.size() - 2)));
        }

        final Pair<Coordinate, Coordinate> arrowEndCoordinates =
                GeometryUtil.arrowEndPoints(new Coordinate(arrowPoint.getY(), arrowPoint.getX()), bearing, length);
        final Pair<Point, Point> arrowLine1 = new Pair<>(mapView.getPoint(arrowPoint), mapView.getPoint(
                new LatLon(arrowEndCoordinates.getFirst().getLat(), arrowEndCoordinates.getFirst().getLon())));
        final Pair<Point, Point> arrowLine2 = new Pair<>(mapView.getPoint(arrowPoint), mapView.getPoint(
                new LatLon(arrowEndCoordinates.getSecond().getLat(), arrowEndCoordinates.getSecond().getLon())));
        return new Pair<>(arrowLine1, arrowLine2);
    }

    private static LatLon arrowPoint(final List<LatLon> points) {
        final double segmentMidLength = points.get(0).greatCircleDistance(points.get(points.size() - 1)) / 2;
        double length = 0;
        int idx = 0;
        for (int i = 0; i < points.size() - 1; i++) {
            final double distance = points.get(0).greatCircleDistance(points.get(i + 1));
            if (distance >= segmentMidLength) {
                idx = i;
                break;
            }
            length += points.get(i).greatCircleDistance(points.get(i + 1));
        }
        final double bearing = Math.toDegrees(points.get(idx).bearing(points.get(idx + 1)));
        final Coordinate coord = GeometryUtil.extrapolate(new Coordinate(points.get(idx).lat(), points.get(idx).lon()),
                bearing, (segmentMidLength - length));
        return new LatLon(coord.getLat(), coord.getLon());
    }
}