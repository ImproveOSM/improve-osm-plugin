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