/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.service.entity;

import java.util.List;
import org.openstreetmap.josm.plugins.improveosm.entity.Comment;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;


/**
 * Defines the body of the comment operation.
 *
 * @author Beata
 * @version $Revision$
 * @param <T> defines the type of target ids
 */
public class CommentRequest<T> {

    private final String username;
    private final String text;
    private final Status status;
    private final List<T> targetIds;


    public CommentRequest(final Comment comment, final List<T> targetIds) {
        this.username = comment.getUsername();
        this.text = comment.getText();
        this.status = comment.getStatus();
        this.targetIds = targetIds;
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

    public List<T> getTargetIds() {
        return targetIds;
    }
}