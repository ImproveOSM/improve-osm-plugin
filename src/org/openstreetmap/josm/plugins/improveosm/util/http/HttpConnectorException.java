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
package org.openstreetmap.josm.plugins.improveosm.util.http;


/**
 * Exception used by the {@code HttpConnector} class.
 *
 * @author Beata
 * @version $Revision$
 */
public class HttpConnectorException extends Exception {

    private static final long serialVersionUID = 6526194581077313322L;

    /**
     * Builds a new object.
     */
    public HttpConnectorException() {}

    /**
     * Builds a new object with the given cause.
     *
     * @param cause the cause of the exception
     */
    public HttpConnectorException(final Throwable cause) {
        super(cause);
    }
}