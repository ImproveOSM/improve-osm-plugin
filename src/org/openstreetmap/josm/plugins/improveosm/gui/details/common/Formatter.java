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
package org.openstreetmap.josm.plugins.improveosm.gui.details.common;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.util.cnf.Config;


/**
 * Utility class, formats custom objects.
 *
 * @author Beata
 * @version $Revision$
 */
public final class Formatter {

    private static final String DOUBLE_FORMAT = "0.00";
    private static final String TSTP = "yyyy-MM-dd HH:mm:ss";
    private static final Long UNIX_TSTP = 1000L;


    private Formatter() {}


    /**
     * Formats the given double value using "0.00" format.
     *
     * @param value a double value
     * @param round if true the value is rounded
     * @return a string representation of the value
     */
    public static String formatDouble(final Double value, final boolean round) {
        return value == null ? "" : (round ? "" + Math.round(value) : new DecimalFormat(DOUBLE_FORMAT).format(value));
    }

    /**
     * Formats the given comment using HTML tags.
     *
     * @param comment a {@code Comment} to be formatted
     * @return a string containing the given {@code Comment}s
     */
    public static String formatComment(final Comment comment) {
        final StringBuilder sb = new StringBuilder("<html><body>");
        sb.append(("<b>"));
        sb.append(formatTimestamp(comment.getTimestamp()));
        sb.append(", ").append(comment.getUsername());
        sb.append("</b><br>");
        final String txt =
                comment.getText() != null ? comment.getText().length() > Config.getInstance().getCommentDisplayLength()
                        ? comment.getText().substring(0, Config.getInstance().getCommentDisplayLength())
                        : comment.getText() : "";
        sb.append(txt);
        if (comment.getStatus() != null) {
            sb.append("changed status to ");
            sb.append(comment.getStatus());
            if (!txt.isEmpty()) {
                sb.append("<br>").append("with ");
                sb.append(txt);
            }

        } else if (!txt.isEmpty()) {
            sb.append("added ");
            sb.append("<br>").append("with ");
            sb.append(txt);
        }

        sb.append("</body></html>");
        return sb.toString();
    }

    /**
     * Formats the given timestamp in 'yyyy-MM-dd HH:mm:ss' format.
     *
     * @param timestamp a {@code Long} value
     * @return a string containing the timestamp
     */
    public static String formatTimestamp(final Long timestamp) {
        final SimpleDateFormat dateTimeFormat = new SimpleDateFormat(TSTP);
        dateTimeFormat.setTimeZone(TimeZone.getDefault());
        return timestamp != null ? dateTimeFormat.format(new Date(timestamp * UNIX_TSTP)) : "";
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
}