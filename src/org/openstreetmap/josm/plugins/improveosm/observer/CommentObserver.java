/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.observer;

import org.openstreetmap.josm.plugins.improveosm.entity.Comment;


/**
 * Observes the comment and status change user actions.
 *
 * @author Beata
 * @version $Revision$
 */
public interface CommentObserver {
    /**
     * Creates a new comment with possible status change to the selected tile(s).
     *
     * @param comment a {@code Comment} object
     */
    void createComment(Comment comment);
}