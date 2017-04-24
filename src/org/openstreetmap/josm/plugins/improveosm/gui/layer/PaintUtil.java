/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
 */
package org.openstreetmap.josm.plugins.improveosm.gui.layer;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.improveosm.util.Util;


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
    private static final int ZOOM_18_MULTIPLY = 2;


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
}