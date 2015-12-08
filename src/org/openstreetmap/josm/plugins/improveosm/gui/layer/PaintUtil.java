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

import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ARROW_ANGLE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ARROW_LENGTH;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.BOTH_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.PARKING_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.PATH_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.POINT_POS_RADIUS;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ROAD_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ROAD_SEGMENT_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ROAD_SEGMENT_SEL_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ROAD_SEGMENT_SEL_STROKE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ROAD_SEGMENT_STROKE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.SEL_ARROW_LENGTH;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TILE_COMPOSITE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TILE_INVALID_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TILE_OPEN_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TILE_SEL_COMPOSITE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TILE_SOLVED_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.WATER_COLOR;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.improveosm.argument.BoundingBox;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;


/**
 * Helper class, holds utility methods used for drawing different domain objects to the map.
 *
 * @author Beata
 * @version $Revision$
 */
final class PaintUtil {

    private PaintUtil() {}


    /**
     * Draws a road segment to the map. A selected road segment is drawn with a different color and stroke.
     *
     * @param graphics the {@code Graphics2D} used for drawing
     * @param mapView the current {@code MapView}
     * @param segment a {@code RoadSegment} object to be drawn
     * @param selected specifies if the segment is selected or not
     */
    static void drawRoadSegment(final Graphics2D graphics, final MapView mapView, final RoadSegment segment,
            final boolean selected) {
        // draw segment
        if (selected) {
            graphics.setStroke(ROAD_SEGMENT_SEL_STROKE);
            graphics.setColor(ROAD_SEGMENT_SEL_COLOR);
        } else {
            graphics.setStroke(ROAD_SEGMENT_STROKE);
            graphics.setColor(ROAD_SEGMENT_COLOR);
        }
        final GeneralPath path = buildPath(graphics, mapView, segment);
        graphics.draw(path);

        // draw arrow
        final Point tip = mapView.getPoint(segment.getPoints().get(segment.getPoints().size() - 1));
        final Point tail = mapView.getPoint(segment.getPoints().get(segment.getPoints().size() - 2));
        final double theta = Math.atan2((tip.getY() - tail.getY()), (tip.getX() - tail.getX()));
        double rho = theta + ARROW_ANGLE;
        final double arrowLength = selected ? SEL_ARROW_LENGTH : ARROW_LENGTH;
        for (int j = 0; j < 2; j++) {
            graphics.draw(new Line2D.Double(tip.getX(), tip.getY(), tip.getX() - arrowLength * Math.cos(rho),
                    tip.getY() - arrowLength * Math.sin(rho)));
            rho = theta - ARROW_ANGLE;
        }
    }

    private static GeneralPath buildPath(final Graphics2D graphics, final MapView mapView, final RoadSegment segment) {
        final GeneralPath path = new GeneralPath();
        Point point = mapView.getPoint(segment.getPoints().get(0));
        path.moveTo(point.getX(), point.getY());
        for (int i = 1; i < segment.getPoints().size(); i++) {
            point = mapView.getPoint(segment.getPoints().get(i));
            path.lineTo(point.getX(), point.getY());
        }
        return path;
    }

    /**
     * Draws a tile to the map. A selected tile is drawn with a stronger composite.
     *
     * @param graphics the {@code Graphics2D} used for drawing
     * @param mapView the current {@code MapView}
     * @param tile a {@code Tile} object to be drawn
     * @param selected specifies if the tile is selected or not
     */
    static void drawTile(final Graphics2D graphics, final MapView mapView, final Tile tile, final boolean selected) {
        // draw tile
        final Color tileColor = tile.getStatus() == Status.OPEN ? TILE_OPEN_COLOR
                : (tile.getStatus() == Status.SOLVED ? TILE_SOLVED_COLOR : TILE_INVALID_COLOR);
        graphics.setColor(tileColor);
        final GeneralPath path = buildPath(graphics, mapView, tile);
        graphics.draw(path);
        final Composite composite = selected ? TILE_SEL_COMPOSITE : TILE_COMPOSITE;
        graphics.setComposite(composite);
        graphics.fill(path);

        // draw points belonging to the tile
        final Color pointColor = pointColor(tile);
        for (final LatLon latLon : tile.getPoints()) {
            drawCircle(graphics, mapView.getPoint(latLon), pointColor, POINT_POS_RADIUS);
        }
    }

    private static GeneralPath buildPath(final Graphics2D g2D, final MapView mv, final Tile tile) {
        final BoundingBox bbox = Util.tileToBoundingBox(tile.getX(), tile.getY());
        final Point northEast = mv.getPoint(new LatLon(bbox.getNorth(), bbox.getEast()));
        final Point northWest = mv.getPoint(new LatLon(bbox.getSouth(), bbox.getWest()));
        final GeneralPath path = new GeneralPath();
        path.moveTo(northEast.getX(), northEast.getY());
        path.lineTo(northWest.getX(), northEast.getY());
        path.lineTo(northWest.getX(), northWest.getY());
        path.lineTo(northEast.getX(), northWest.getY());
        path.lineTo(northEast.getX(), northEast.getY());
        return path;
    }

    private static Color pointColor(final Tile tile) {
        Color color;
        switch (tile.getType()) {
            case ROAD:
                color = ROAD_COLOR;
                break;
            case PARKING:
                color = PARKING_COLOR;
                break;
            case WATER:
                color = WATER_COLOR;
                break;
            case PATH:
                color = PATH_COLOR;
                break;
            default:
                color = BOTH_COLOR;
                break;
        }
        return color;
    }

    /**
     * Draws a circle with the specified settings.
     *
     * @param graphics the {@code Graphics2D} used for drawing
     * @param point the middle point
     * @param color the circle's color
     * @param radius the circle's radius
     */
    static void drawCircle(final Graphics2D graphics, final Point2D point, final Color color, final double radius) {
        final Ellipse2D.Double circle = new Ellipse2D.Double(point.getX(), point.getY(), radius, radius);
        graphics.setColor(color);
        graphics.fill(circle);
        graphics.draw(circle);
    }

    static void drawTunRestriction(final Graphics2D graphics, final MapView mapView,
            final TurnRestriction turnRestriction, final boolean selected) {
        final Point point = mapView.getPoint(turnRestriction.getPoint());
        if (mapView.contains(point)) {
            drawIcon(graphics, IconConfig.getInstance().getTurnRestrictionIcon(), point);
        }
    }

    private static void drawIcon(final Graphics2D g2D, final ImageIcon icon, final Point p) {
        g2D.drawImage(icon.getImage(), p.x - (icon.getIconWidth() / 2), p.y - (icon.getIconHeight() / 2),
                new ImageObserver() {

            @Override
            public boolean imageUpdate(final Image img, final int infoflags, final int x, final int y,
                    final int width, final int height) {
                return false;
            }
        });
    }

}