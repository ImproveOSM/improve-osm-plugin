/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.observer;

/**
 * Observes the comment and status change user actions.
 *
 * @author nicoletav
 * @version $Revision$
 */
public interface DownloadWayObservable {

    /**
     *
     * @param observer
     */
    void addObserver(final DownloadWayObserver observer);

    /**
     *
     */
    void notifyObserver();
}