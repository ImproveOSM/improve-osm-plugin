/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.layer;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.HashMap;
import java.util.Map;


/**
 * Defines static properties used for drawing domain entities to the map.
 *
 * @author Beata
 * @version $Revision$
 */
final class Constants {

    /* constant used for drawing clusters */
    static final double CLUSTER_DEF_RADIUS = 50;
    static final Color DIRECTIONOFFLOW_CLUSTER_COLOR = new Color(244, 127, 34);
    static final Color MISSINGGEO_CLUSTER_COLOR = new Color(75, 184, 92);
    static final Color TURNRESTRICTION_CLUSTER_COLOR = new Color(60, 111, 183);

    /* constants used for drawing DirectionOfFlow segments */
    static final Color ROAD_SEGMENT_COLOR = new Color(238, 118, 0);
    static final Color ROAD_SEGMENT_SEL_COLOR = new Color(220, 20, 60);
    static final Stroke ROAD_SEGMENT_STROKE = new BasicStroke(5F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static final Stroke ROAD_SEGMENT_SEL_STROKE = new BasicStroke(10F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static final double ARROW_ANGLE = Math.toRadians(40);
    static final double ARROW_LENGTH = 1;
    static final double SEL_ARROW_LENGTH = 2;

    /* constants used for drawing MissingGeometry tiles */
    static final Stroke TILE_LINE_STROKE = new BasicStroke(4F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static final Color TILE_OPEN_COLOR = new Color(70, 130, 180);
    static final Color TILE_SOLVED_COLOR = new Color(60, 179, 113);
    static final Color TILE_INVALID_COLOR = new Color(255, 99, 71);
    static final Color ROAD_COLOR = new Color(180, 82, 205);
    static final Color PARKING_COLOR = new Color(238, 238, 0);
    static final Color BOTH_COLOR = new Color(255, 165, 0);
    static final Color POINT_COLOR = new Color(30, 30, 30);
    static final Color WATER_COLOR = new Color(0, 154, 205);
    static final Color PATH_COLOR = new Color(160, 82, 45);
    static final int POINT_POS_RADIUS = 4;
    static final int SEL_POINT_POS_RADIUS = 9;

    /* constants used for drawing TurnRestriction segments */
    static final Color TURN_SEGMENT_FROM_COLOR = new Color(0, 255, 0);
    static final Color TURN_SEGMENT_TO_COLOR = new Color(255, 0, 0);
    static final Stroke TURN_SEGMENT_STROKE = new BasicStroke(5F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    static final double TURN_ARROW_LENGTH = 1.5;
    static final Color COMPLEX_TURN_COLOR = new Color(60, 111, 183);
    static final int COMPLEX_TURN_RADIUS = 20;
    static final int COMPLEX_TURN_SEL_RADIUS = 30;
    static final float COMPLEX_TURN_FONT_SIZE = 11F;
    static final float TURN_SEGMENT_FONT_SIZE = 12F;

    /* constants used for drawing a label containing a string */
    static final Composite LABEL_COMPOSITE = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.90F);
    static final Color LABEL_BACKGROUND_COLOR = Color.white;
    static final int LABEL_DIST = 20;

    static final Map<RenderingHints.Key, Object> RENDERING_MAP = createRenderingMap();

    private Constants() {}


    private static Map<RenderingHints.Key, Object> createRenderingMap() {
        final Map<RenderingHints.Key, Object> map = new HashMap<>();
        map.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        map.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        map.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        return map;
    }
}