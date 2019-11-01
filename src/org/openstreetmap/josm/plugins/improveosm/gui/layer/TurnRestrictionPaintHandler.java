/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.layer;

import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.COMPLEX_TURN_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.COMPLEX_TURN_FONT_SIZE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.COMPLEX_TURN_RADIUS;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.COMPLEX_TURN_SEL_RADIUS;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.LABEL_BACKGROUND_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.LABEL_COMPOSITE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.LABEL_DIST;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TURNRESTRICTION_CLUSTER_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TURN_ARROW_LENGTH;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TURN_SEGMENT_FONT_SIZE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TURN_SEGMENT_FROM_COLOR;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TURN_SEGMENT_STROKE;
import static org.openstreetmap.josm.plugins.improveosm.gui.layer.Constants.TURN_SEGMENT_TO_COLOR;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.gui.MapView;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnSegment;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.IconConfig;
import com.telenav.josm.common.entity.Pair;
import com.telenav.josm.common.gui.PaintManager;


/**
 * Handles turn restriction drawing.
 *
 * @author beataj
 * @version $Revision$
 */
final class TurnRestrictionPaintHandler extends PaintHandler<TurnRestriction> {

    @Override
    void drawItem(final Graphics2D graphics, final MapView mapView, final TurnRestriction turnRestriction,
            final boolean selected) {
        final Point point = mapView.getPoint(turnRestriction.getPoint());
        if (turnRestriction.getTurnRestrictions() != null) {
            // draw complex turn restriction

            final int radius = selected ? COMPLEX_TURN_SEL_RADIUS : COMPLEX_TURN_RADIUS;
            PaintManager.drawCircle(graphics, point, COMPLEX_TURN_COLOR, radius);
            PaintManager.drawText(graphics, Integer.toString(turnRestriction.getTurnRestrictions().size()), point,
                    mapView.getFont().deriveFont(Font.BOLD, COMPLEX_TURN_FONT_SIZE), Color.white);
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
                PaintManager.drawIcon(graphics, icon, point);
            }
        }
    }

    @Override
    Color getClusterColor() {
        return TURNRESTRICTION_CLUSTER_COLOR;
    }

    private static void drawTurnSegments(final Graphics2D graphics, final MapView mapView,
            final TurnRestriction turnRestriction) {
        graphics.setFont(mapView.getFont().deriveFont(Font.BOLD));
        if (turnRestriction.getSegments() != null) {
            // draw segments
            final List<TurnSegment> turnSegments = orderTurnSegments(turnRestriction);
            for (int i = 0; i < turnSegments.size(); i++) {
                final Color color = i == 0 ? TURN_SEGMENT_FROM_COLOR : TURN_SEGMENT_TO_COLOR;
                final double arrowLength = PaintUtil.arrowLength(mapView, TURN_ARROW_LENGTH);
                final List<Point> geometry = PaintUtil.toPoints(mapView, turnSegments.get(i).getPoints());
                if (i == 0 || i == turnSegments.size() - 1) {
                    final boolean isFromSegment = i == 0;

                    final Pair<Pair<Point, Point>, Pair<Point, Point>> arrowGeometry = PaintUtil.arrowGeometry(mapView,
                            turnSegments.get(i).getPoints(), isFromSegment, arrowLength);
                    PaintManager.drawDirectedSegment(graphics, geometry, arrowGeometry, color, TURN_SEGMENT_STROKE);
                } else {
                    PaintManager.drawSegment(graphics, geometry, color, TURN_SEGMENT_STROKE);
                }
            }

            // draw labels
            final TurnSegment firstSegment = turnRestriction.getSegments().get(0);
            final Point firstLabelPoint = labelPoint(mapView, firstSegment.getPoints(), true);
            final Font font = mapView.getFont().deriveFont(Font.BOLD, TURN_SEGMENT_FONT_SIZE);
            PaintManager.drawText(graphics, Integer.toString(firstSegment.getNumberOfTrips()), firstLabelPoint, font,
                    LABEL_BACKGROUND_COLOR, Color.black, LABEL_COMPOSITE);

            final TurnSegment lastSegment = turnRestriction.getSegments().get(turnRestriction.getSegments().size() - 1);
            Point secondLabelPoint = labelPoint(mapView, lastSegment.getPoints(), false);
            secondLabelPoint = removeOverlap(secondLabelPoint, firstLabelPoint);
            PaintManager.drawText(graphics, Integer.toString(turnRestriction.getNumberOfPasses()), secondLabelPoint,
                    font, LABEL_BACKGROUND_COLOR, Color.black, LABEL_COMPOSITE);
        }
    }

    private static Point removeOverlap(final Point secondLabelPoint, final Point firstLabelPoint) {
        if (secondLabelPoint.equals(firstLabelPoint)) {
            secondLabelPoint.x -= LABEL_DIST;
        }
        return secondLabelPoint;
    }

    private static List<TurnSegment> orderTurnSegments(final TurnRestriction turnRestriction) {
        final List<TurnSegment> result = new ArrayList<>();
        for (int i = 0; i < turnRestriction.getSegments().size(); i++) {
            final TurnSegment segment = turnRestriction.getSegments().get(i);
            if (segment.getPoints().get(segment.getPoints().size() - 1).equals(turnRestriction.getPoint())) {
                // from segment
                result.add(0, segment);
            } else if (segment.getPoints().get(0).equals(turnRestriction.getPoint())) {
                // first to segment
                result.add(1, segment);
            } else {
                final int index = getIndex(result, segment);
                result.add(index, segment);
            }
        }

        return result;
    }

    private static int getIndex(final List<TurnSegment> turnSegments, final TurnSegment turnSegment) {
        int index = 0;
        for (int i = 0; i < turnSegments.size(); i++) {
            if (turnSegment.getPoints().get(0)
                    .equals(turnSegments.get(i).getPoints().get(turnSegments.get(i).getPoints().size() - 1))) {
                index = i;
                break;
            }
        }
        return index + 1;
    }

    private static Point labelPoint(final MapView mapView, final List<LatLon> points, final boolean isFromSegment) {
        Point labelPoint;
        if (points.size() == 2) {
            labelPoint = mapView.getPoint(points.get(0).getCenter(points.get(1)));
        } else {
            labelPoint = mapView.getPoint(points.get(points.size() / 2));
        }
        return labelPoint;
    }
}