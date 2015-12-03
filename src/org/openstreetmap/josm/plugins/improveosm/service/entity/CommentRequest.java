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
package org.openstreetmap.josm.plugins.improveosm.service.entity;

import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;


/**
 * Defines the body of the comment operation.
 *
 * @author Beata
 * @version $Revision$
 */
public class CommentRequest {

    private final String username;
    private final String text;
    private final Status status;


    public CommentRequest(final Comment comment) {
        this.username = comment.getUsername();
        this.text = comment.getText();
        this.status = comment.getStatus();
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public Status getStatus() {
        return status;
    }
}