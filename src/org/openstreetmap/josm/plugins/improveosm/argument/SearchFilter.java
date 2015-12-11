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
package org.openstreetmap.josm.plugins.improveosm.argument;

import org.openstreetmap.josm.plugins.improveosm.entity.Status;


/**
 * Defines a general filter that can be applied to the data layers.
 *
 * @author Beata
 * @version $Revision$
 */
public class SearchFilter {

    public static final SearchFilter DEFAULT = new SearchFilter(Status.OPEN);
    private final Status status;


    public SearchFilter(final Status status) {
        this.status = status;
    }


    public Status getStatus() {
        return status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj instanceof SearchFilter) {
            final SearchFilter other = (SearchFilter) obj;
            result = (status == null && other.getStatus() == null)
                    || (status != null && status.equals(other.getStatus()));

        }
        return result;
    }
}