/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.entity;

/**
 * Defines the attributes of a comment.
 *
 * @author Beata
 * @version $Revision$
 */
public class Comment {

    private final String username;
    private final Long timestamp;
    private final String text;
    private final Status status;


    /**
     * Builds a new object with the given arguments.
     *
     * @param username the user's OSM username
     * @param timestamp the time when the comment was created
     * @param text the text of the comment
     * @param status the status of the road segment set by this comment
     */
    public Comment(final String username, final Long timestamp, final String text, final Status status) {
        this.username = username;
        this.timestamp = timestamp;
        this.text = text;
        this.status = status;
    }


    public String getUsername() {
        return username;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public String getText() {
        return text;
    }

    public Status getStatus() {
        return status;
    }
}