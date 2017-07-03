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
package org.openstreetmap.josm.plugins.improveosm.util;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.data.coor.EastNorth;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import com.telenav.josm.common.argument.BoundingBox;


/**
 * Holds utility methods.
 *
 * @author Beata
 * @version $Revision$
 */
public final class Util {

    private static final double POZ_DIST = 15.0;
    private static final double SEG_DIST = 4.0;
    private static final int ZOOM1_SCALE = 78206;
    private static final int DEGREE_180 = 180;
    private static final int DEGREE_360 = 360;
    private static final int MIN_ZOOM = 0;
    private static final int MAX_ZOOM = 22;
    private static final int MAX_TILE_ZOOM = 18;
    private static final int TILE_SIZE = 1024;


    private Util() {}


    /**
     * Returns the zoom level based on the given bounds.
     *
     * @param bounds the map bounds
     * @return an integer
     */
    public static int zoom(final Bounds bounds) {
        return Main.map.mapView.getScale() >= ZOOM1_SCALE ? 1 : (int) Math.min(MAX_ZOOM,
                Math.max(MIN_ZOOM, Math.round(Math.floor(Math.log(TILE_SIZE / bounds.asRect().height) / Math.log(2)))));
    }

    /**
     * Computes the bounding box corresponding to the given tile identifiers.
     *
     * @param x the tile's X identifier
     * @param y the tile's Y identifier
     * @return a {@code BoundingBox} object
     */
    public static BoundingBox tileToBoundingBox(final Integer x, final Integer y) {
        final double north = tile2lat(y);
        final double south = tile2lat(y + 1);
        final double west = tile2lon(x);
        final double east = tile2lon(x + 1);
        return new BoundingBox(north, south, east, west);
    }

    private static double tile2lat(final int y) {
        final double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, MAX_TILE_ZOOM);
        return Math.toDegrees(Math.atan(Math.sinh(n)));
    }

    private static double tile2lon(final int x) {
        return x / Math.pow(2.0, MAX_TILE_ZOOM) * DEGREE_360 - DEGREE_180;
    }

    /**
     * Returns the segment nearby the given point. If there is no road segment near the method returns null.
     *
     * @param roadSegments a list of {@code RoadSegment}s
     * @param point the location where the user clicked
     * @return a {@code RoadSegment}
     */
    public static RoadSegment nearbyRoadSegment(final List<RoadSegment> roadSegments, final Point point) {
        RoadSegment result = null;
        double minDistance = Double.MAX_VALUE;
        for (final RoadSegment roadSegment : roadSegments) {
            final double distance = minDistance(roadSegment.getPoints(), point);
            if (distance <= minDistance) {
                minDistance = distance;
                result = roadSegment;
            }
        }
        if (minDistance > SEG_DIST) {
            result = null;
        }
        return result;
    }

    private static double minDistance(final List<LatLon> points, final Point point) {
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < points.size() - 1; i++) {
            final Point start = Main.map.mapView.getPoint(points.get(i));
            final Point end = Main.map.mapView.getPoint(points.get(i + 1));
            final double distance = distance(point, start, end);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        return minDistance;
    }

    private static double distance(final Point2D point, final Point2D start, final Point2D end) {
        final double xD = end.getX() - start.getX();
        final double yD = end.getY() - start.getY();
        final double u =
                ((point.getX() - start.getX()) * xD + (point.getY() - start.getY()) * yD) / (xD * xD + yD * yD);
        final Point2D nearestPoint =
                u < 0 ? start : ((u > 1) ? end : new Point2D.Double(start.getX() + u * xD, start.getY() + u * yD));
        return nearestPoint.distance(point);
    }



    /**
     * Returns the tile corresponding to the given point.
     *
     * @param tiles a {@code Tile} a list of available tiles
     * @param point a {@code Point} represents the location where the user clicked on the map
     * @return a {@code Tile} object
     */
    public static Tile nearbyTile(final List<Tile> tiles, final Point point) {
        Tile result = null;
        final LatLon latLon = pointToLatLon(point);
        final int tileX = getTileX(latLon.getX());
        final int tileY = getTileY(latLon.getY());
        if (tiles != null) {
            for (final Tile tile : tiles) {
                if (tile.equals(new Tile(tileX, tileY))) {
                    result = tile;
                    break;
                }
            }
        }
        return result;
    }

    public static LatLon pointToLatLon(final Point point) {
        final int width = Main.map.mapView.getWidth();
        final int height = Main.map.mapView.getHeight();
        final EastNorth center = Main.map.mapView.getCenter();
        final double scale = Main.map.mapView.getScale();
        final EastNorth eastNorth = new EastNorth(center.east() + (point.getX() - width / 2.0) * scale,
                center.north() - (point.getY() - height / 2.0) * scale);
        return Main.map.mapView.getProjection().eastNorth2latlon(eastNorth);
    }

    private static int getTileX(final double lon) {
        int tileX = (int) Math.floor((lon + DEGREE_180) / DEGREE_360 * (1 << MAX_TILE_ZOOM));
        if (tileX >= (1 << MAX_TILE_ZOOM)) {
            tileX = (1 << MAX_TILE_ZOOM) - 1;
        }
        return tileX;
    }

    private static Integer getTileY(final double lat) {
        int tileY = (int) Math
                .floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2
                        * (1 << MAX_TILE_ZOOM));
        if (tileY >= (1 << MAX_TILE_ZOOM)) {
            tileY = (1 << MAX_TILE_ZOOM) - 1;
        }
        return tileY;
    }

    /**
     * Verify if a tile intersects a bounding box. Return true or false depending on the intersection relation between
     * the tile and the bounding box
     */
    public static boolean tileIntersectsBoundingBox(final Tile tile, final Rectangle2D boundingBox) {
        return boundingBox.intersects(buildRectangleFromCoordinates(tile2lon(tile.getX()), tile2lat(tile.getY()),
                tile2lon(tile.getX() + 1), tile2lat(tile.getY() + 1)));
    }

    public static Rectangle2D buildRectangleFromCoordinates(final double coord1X, final double coord1Y,
            final double coord2X, final double coord2Y) {
        return new Rectangle2D.Double(Math.min(coord1X, coord2X), Math.min(coord1Y, coord2Y),
                Math.abs(coord1X - coord2X), Math.abs(coord1Y - coord2Y));
    }

    /**
     * Returns the turn restriction corresponding to the given point.
     *
     * @param turnRestrictions a list of available {@code TurnRestriction}s
     * @param point a {@code Point} represents the location where the user clicked on the map
     * @return a {@code TurnRestriction} object
     */
    public static TurnRestriction nearbyTurnRestriction(final List<TurnRestriction> turnRestrictions,
            final Point point) {
        double minDist = Double.MAX_VALUE;
        TurnRestriction result = null;
        if (turnRestrictions != null) {
            for (final TurnRestriction turnRestriction : turnRestrictions) {
                final double dist = distance(point, turnRestriction.getPoint());
                if (dist <= minDist && dist <= POZ_DIST) {
                    minDist = dist;
                    result = turnRestriction;
                }
            }
        }
        return result;
    }

    private static double distance(final Point2D fromPoint, final LatLon toLatLon) {
        final Point toPoint = Main.map.mapView.getPoint(toLatLon);
        return new Point2D.Double(fromPoint.getX(), fromPoint.getY()).distance(toPoint);
    }
}