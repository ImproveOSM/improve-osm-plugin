/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.service;

/**
 * Custom exception to be used by {@code Service}.
 *
 * @author Beata
 * @version $Revision$
 */
public class ServiceException extends Exception {

    private static final long serialVersionUID = 7451524925241196811L;

    public ServiceException(final Exception cause) {
        super(cause);
    }

    public ServiceException(final String msg) {
        super(msg);
    }
}