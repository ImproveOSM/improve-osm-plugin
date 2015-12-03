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