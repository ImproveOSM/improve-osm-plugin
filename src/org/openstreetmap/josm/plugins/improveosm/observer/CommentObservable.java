/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.observer;

import org.openstreetmap.josm.plugins.improveosm.entity.Comment;


/**
 * The observable interface for the {@code CommentObserver} object.
 *
 * @author Beata
 * @version $Revision$
 */
public interface CommentObservable {

    /**
     * Notifies the registered observer. As a result a new comment will be created to the selected tile(s).
     *
     * @param comment a {@code Comment}
     */
    void notifyObserver(Comment comment);

    /**
     * Registers the given observer.
     *
     * @param observer a {@code CommentObserver}
     */
    void registerObserver(CommentObserver observer);
}