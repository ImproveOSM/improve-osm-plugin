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
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.COMPLEX_TURN_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.COMPLEX_TURN_FONT_SIZE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.COMPLEX_TURN_RADIUS;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.COMPLEX_TURN_SEL_RADIUS;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.LABEL_BACKGROUND_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.LABEL_COMPOSITE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.LABEL_DIST;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.NORMAL_COMPOSITE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.PARKING_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.PATH_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.POINT_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.POINT_POS_RADIUS;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ROAD_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ROAD_SEGMENT_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ROAD_SEGMENT_SEL_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ROAD_SEGMENT_SEL_STROKE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.ROAD_SEGMENT_STROKE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.SEL_ARROW_LENGTH;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.SEL_POINT_POS_RADIUS;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TILE_COMPOSITE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TILE_INVALID_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TILE_LINE_STROKE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TILE_OPEN_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TILE_SEL_COMPOSITE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TILE_SOLVED_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TURN_ARROW_LENGTH;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TURN_SEGMENT_FONT_SIZE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TURN_SEGMENT_FROM_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TURN_SEGMENT_STROKE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TURN_SEGMENT_TO_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.WATER_COLOR;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.util.List;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.improveosm.argument.BoundingBox;
import org.openstreetmap.josm.plugins.improveosm.entity.RoadSegment;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.Tile;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnSegment;
import org.openstreetmap.josm.plugins.improveosm.util.Util;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import org.openstreetmap.josm.tools.Pair;


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
        final GeneralPath path = buildPath(mapView, segment.getPoints());
        graphics.draw(path);

        // draw arrow
        final double arrowLength = selected ? SEL_ARROW_LENGTH : ARROW_LENGTH;
        drawArrow(graphics, mapView, segment.getPoints().get(segment.getPoints().size() - 1),
                segment.getPoints().get(segment.getPoints().size() - 2), arrowLength);
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
        final Color borderColor = tile.getStatus() == Status.OPEN ? TILE_OPEN_COLOR
                : (tile.getStatus() == Status.SOLVED ? TILE_SOLVED_COLOR : TILE_INVALID_COLOR);
        final Color tileColor = tileColor(tile);

        // draw tile border
        graphics.setComposite(NORMAL_COMPOSITE);
        graphics.setStroke(TILE_LINE_STROKE);
        graphics.setColor(borderColor);
        final GeneralPath path = buildPath(mapView, tile);
        graphics.draw(path);

        // fill the tile
        final Composite composite = selected ? TILE_SEL_COMPOSITE : TILE_COMPOSITE;
        graphics.setColor(tileColor);
        graphics.setComposite(composite);
        graphics.fill(path);
        final int radius = selected ? SEL_POINT_POS_RADIUS : POINT_POS_RADIUS;
        // draw points belonging to the tile
        if (tile.getPoints() != null && tile.getPoints().size() > 1) {
            for (final LatLon latLon : tile.getPoints()) {
                drawCircle(graphics, mapView.getPoint(latLon), POINT_COLOR, radius);
            }
        }
    }

    private static GeneralPath buildPath(final MapView mv, final Tile tile) {
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

    private static Color tileColor(final Tile tile) {
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
    static void drawCircle(final Graphics2D graphics, final Point point, final Color color, final int radius) {
        graphics.setColor(color);
        graphics.fillOval(point.x - radius / 2, point.y - radius / 2, radius, radius);
    }

    /**
     * Draws a turn restriction to the map. If a turn restriction is selected then also the turn segments with
     * corresponding number of trips are drawn to the map.
     *
     * @param graphics the {@code Graphics2D} used for drawing
     * @param mapView the current {@code MapView}
     * @param turnRestriction the {@code TurnRestriction} to be drawn to the map
     * @param selected specifies if the turn restriction is selected or not
     */
    static void drawTunRestriction(final Graphics2D graphics, final MapView mapView,
            final TurnRestriction turnRestriction, final boolean selected) {
        final Point point = mapView.getPoint(turnRestriction.getPoint());
        if (turnRestriction.getTurnRestrictions() != null) {
            // draw complex turn restriction

            final int radius = selected ? COMPLEX_TURN_SEL_RADIUS : COMPLEX_TURN_RADIUS;
            drawCircle(graphics, point, COMPLEX_TURN_COLOR, radius);
            drawTurnRestrictionCount(graphics, mapView, turnRestriction.getTurnRestrictions().size(), point);
        } else {
            // draw simple turn restriction

            if (selected) {
                // draw turn segments
                drawTurnSegments(graphics, mapView, turnRestriction);
            }

            // draw turn restriction
            if (mapView.contains(point)) {
                final ImageIcon icon = selected ? IconConfig.getInstance().getSelectedTurnRestrictionIcon()
                        : IconConfig.getInstance().getTurnRestrictionIcon();
                drawIcon(graphics, icon, point);
            }
        }
    }

    private static void drawTurnRestrictionCount(final Graphics2D graphics, final MapView mapView, final int count,
            final Point point) {
        graphics.setColor(Color.white);
        graphics.setFont(mapView.getFont().deriveFont(Font.BOLD, COMPLEX_TURN_FONT_SIZE));
        final FontMetrics fm = mapView.getFontMetrics(mapView.getFont().deriveFont(Font.BOLD, COMPLEX_TURN_FONT_SIZE));
        final String text = "" + count;
        graphics.drawString(text, point.x - fm.stringWidth(text) / 2, point.y - fm.getHeight() / 2 + fm.getAscent());
    }

    private static void drawTurnSegments(final Graphics2D graphics, final MapView mapView,
            final TurnRestriction turnRestriction) {
        graphics.setFont(mapView.getFont().deriveFont(Font.BOLD));
        if (turnRestriction.getSegments() != null) {
            graphics.setStroke(TURN_SEGMENT_STROKE);

            // draw segments
            for (int i = 0; i < turnRestriction.getSegments().size(); i++) {
                final Color color = i == 0 ? TURN_SEGMENT_FROM_COLOR : TURN_SEGMENT_TO_COLOR;
                graphics.setColor(color);
                graphics.draw(buildPath(mapView, turnRestriction.getSegments().get(i).getPoints()));
                final Pair<LatLon, LatLon> pair = tipTailPoints(turnRestriction.getSegments().get(i).getPoints());
                drawArrow(graphics, mapView, pair.a, pair.b, TURN_ARROW_LENGTH);
            }

            // draw labels
            final TurnSegment firstSegment = turnRestriction.getSegments().get(0);
            drawText(graphics, mapView, firstSegment.getPoints(), "" + firstSegment.getNumberOfTrips());
            final TurnSegment lastSegment = turnRestriction.getSegments().get(turnRestriction.getSegments().size() - 1);
            drawText(graphics, mapView, lastSegment.getPoints(), "" + turnRestriction.getNumberOfPasses());

        }
    }

    private static Pair<LatLon, LatLon> tipTailPoints(final List<LatLon> points) {
        LatLon tip;
        LatLon tail;
        if (points.size() > 2) {
            final int idx = points.size() / 2;
            tip = points.get(idx);
            tail = points.get(idx - 1);
        } else {
            tail = points.get(0);
            tip = new LatLon((points.get(1).lat() + points.get(0).lat()) / 2,
                    (points.get(1).lon() + points.get(0).lon()) / 2);
        }
        return new Pair<LatLon, LatLon>(tip, tail);
    }

    private static void drawText(final Graphics2D graphics, final MapView mapView, final List<LatLon> points,
            final String txt) {
        final Pair<LatLon, LatLon> tipTailPoints = tipTailPoints(points);
        final Point labelPoint = mapView.getPoint(tipTailPoints.a);
        final int cmp = Double.compare(tipTailPoints.a.getX(), tipTailPoints.b.getX());
        if (cmp == 0) {
            labelPoint.x += LABEL_DIST;
        } else if (cmp < 0) {
            labelPoint.x -= LABEL_DIST;
            labelPoint.y -= LABEL_DIST;
        } else {
            labelPoint.x += LABEL_DIST;
            labelPoint.y += LABEL_DIST;
        }
        graphics.setFont(mapView.getFont().deriveFont(Font.BOLD, TURN_SEGMENT_FONT_SIZE));
        final FontMetrics fontMetrics =
                mapView.getFontMetrics(mapView.getFont().deriveFont(Font.BOLD, TURN_SEGMENT_FONT_SIZE));
        final Rectangle2D rect = fontMetrics.getStringBounds(txt, graphics);
        graphics.setComposite(LABEL_COMPOSITE);
        graphics.setColor(LABEL_BACKGROUND_COLOR);
        graphics.setStroke(ROAD_SEGMENT_STROKE);
        graphics.fillRect(labelPoint.x, labelPoint.y - fontMetrics.getAscent(), (int) rect.getWidth(),
                (int) rect.getHeight());
        graphics.setComposite(NORMAL_COMPOSITE);
        graphics.setColor(Color.black);
        graphics.drawString(txt, labelPoint.x, labelPoint.y);
        graphics.setComposite(NORMAL_COMPOSITE);
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


    /* commonly used private methods */

    private static void drawArrow(final Graphics2D graphics, final MapView mapView, final LatLon tipLatLon,
            final LatLon tailLatLon, final double arrowLength) {
        // draw arrow
        final Point tip = mapView.getPoint(tipLatLon);
        final Point tail = mapView.getPoint(tailLatLon);
        final double theta = Math.atan2((tip.getY() - tail.getY()), (tip.getX() - tail.getX()));
        double rho = theta + ARROW_ANGLE;
        for (int j = 0; j < 2; j++) {
            graphics.draw(new Line2D.Double(tip.getX(), tip.getY(), tip.getX() - arrowLength * Math.cos(rho),
                    tip.getY() - arrowLength * Math.sin(rho)));
            rho = theta - ARROW_ANGLE;
        }
    }

    private static GeneralPath buildPath(final MapView mapView, final List<LatLon> points) {
        final GeneralPath path = new GeneralPath();
        Point point = mapView.getPoint(points.get(0));
        path.moveTo(point.getX(), point.getY());
        for (int i = 1; i < points.size(); i++) {
            point = mapView.getPoint(points.get(i));
            path.lineTo(point.getX(), point.getY());
        }
        return path;
    }
}