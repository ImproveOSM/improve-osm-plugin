/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.gui.details.common;

import org.openstreetmap.josm.data.coor.LatLon;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;
import com.telenav.josm.common.entity.Pair;
import com.telenav.josm.common.formatter.EntityFormatter;


/**
 * Utility class, formats custom objects.
 *
 * @author Beata
 * @version $Revision$
 */
public final class Formatter {

    private Formatter() {}


    /**
     * Formats the given comment using HTML tags.
     *
     * @param comment a {@code Comment} to be formatted
     * @return a string containing the given {@code Comment}s
     */
    public static String formatComment(final Comment comment) {
        final StringBuilder sb = new StringBuilder("<html><body>");
        sb.append("<b>");
        sb.append(EntityFormatter.formatTimestamp(comment.getTimestamp()));
        sb.append(", ").append(comment.getUsername());
        sb.append("</b><br>");
        final String txt =
                comment.getText() != null ? comment.getText().length() > Config.getInstance().getCommentDisplayLength()
                        ? comment.getText().substring(0, Config.getInstance().getCommentDisplayLength())
                        : comment.getText() : "";
        if (comment.getStatus() != null) {
            sb.append("changed status to ");
            sb.append(comment.getStatus());
            if (!txt.isEmpty()) {
                sb.append("<br>").append("with comment:");
                sb.append(txt);
            }

        } else if (!txt.isEmpty()) {
            sb.append("added comment:").append(txt);
        }

        sb.append("</body></html>");
        return sb.toString();
    }


    /**
     * Formats the given string representing a turn type. The method removes all the '_' characters and transforms the
     * given value to lower case.
     *
     * @param turnType a {@code String}
     * @return a formatted string
     */
    public static String formatTurnType(final String turnType) {
        return turnType.toLowerCase().replace("_", " ");
    }

    /**
     * Formats the given coordinate in 'latitude,longitude' format.
     *
     * @param latLon a {@code LatLon} object
     * @return a formatted string
     */
    public static String formatLatLon(final LatLon latLon) {
        return latLon == null ? "" : latLon.getY() + "," + latLon.getX();
    }

    /**
     * Formats the given Pair object in 'first segment trips number/last segment trips number' format.
     *
     * @param value a {@code Pair} object representing the number of the first segment trips and the number of the last
     * segment trips
     * @return a formatted string
     */
    public static String formatFirstLastTripsNumber(final Pair<Integer, Integer> value) {
        return value.getFirst() + "/" + value.getSecond();
    }
}