/*
 * The code is licensed under the LGPL Version 3 license http://www.gnu.org/licenses/lgpl-3.0.en.html.
 *
 * The collected imagery is protected & available under the CC BY-SA version 4 International license.
 * https://creativecommons.org/licenses/by-sa/4.0/legalcode.
 *
 * Copyright ©2017, Telenav, Inc. All Rights Reserved
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
            for (int i = 0; i < turnRestriction.getSegments().size(); i++) {
                final Color color = i == 0 ? TURN_SEGMENT_FROM_COLOR : TURN_SEGMENT_TO_COLOR;
                final double arrowLength = PaintUtil.arrowLength(mapView, TURN_ARROW_LENGTH);
                final List<Point> geometry =
                        PaintUtil.toPoints(mapView, turnRestriction.getSegments().get(i).getPoints());
                if (i == 0 || i == turnRestriction.getSegments().size() - 1) {
                    final boolean isFromSegment = i == 0;

                    final Pair<Pair<Point, Point>, Pair<Point, Point>> arrowGeometry = PaintUtil.arrowGeometry(mapView,
                            turnRestriction.getSegments().get(i).getPoints(), isFromSegment, arrowLength);
                    PaintManager.drawDirectedSegment(graphics, geometry, arrowGeometry, color, TURN_SEGMENT_STROKE);
                } else {
                    PaintManager.drawSegment(graphics, geometry, color, TURN_SEGMENT_STROKE);
                }
            }

            // draw labels
            final TurnSegment firstSegment = turnRestriction.getSegments().get(0);
            Point labelPoint = labelPoint(mapView, firstSegment.getPoints(), true);
            final Font font = mapView.getFont().deriveFont(Font.BOLD, TURN_SEGMENT_FONT_SIZE);
            PaintManager.drawText(graphics, Integer.toString(firstSegment.getNumberOfTrips()), labelPoint, font,
                    LABEL_BACKGROUND_COLOR, Color.black, LABEL_COMPOSITE);

            final TurnSegment lastSegment = turnRestriction.getSegments().get(turnRestriction.getSegments().size() - 1);
            labelPoint = labelPoint(mapView, lastSegment.getPoints(), true);
            PaintManager.drawText(graphics, Integer.toString(turnRestriction.getNumberOfPasses()), labelPoint, font,
                    LABEL_BACKGROUND_COLOR, Color.black, LABEL_COMPOSITE);
        }
    }


    private static Point labelPoint(final MapView mapView, final List<LatLon> points, final boolean isFromSegment) {
        final Point labelPoint =
                isFromSegment ? mapView.getPoint(points.get(0)) : mapView.getPoint(points.get(points.size() - 1));
                final int cmp = Double.compare(mapView.getPoint(points.get(0)).getX(),
                        mapView.getPoint(points.get(points.size() - 1)).getX());
                if (cmp == 0) {
                    labelPoint.x += LABEL_DIST;
                } else if (cmp < 0) {
                    labelPoint.x -= LABEL_DIST;
                    labelPoint.y -= LABEL_DIST;
                } else {
                    labelPoint.x += LABEL_DIST;
                    labelPoint.y += LABEL_DIST;
                }
                return labelPoint;
    }
}