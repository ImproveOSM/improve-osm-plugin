/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.argument;

import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import com.telenav.josm.common.entity.EntityUtil;


/**
 * Defines a general filter that can be applied to the data layers.
 *
 * @author Beata
 * @version $Revision$
 */
public class SearchFilter {

    public static final SearchFilter DEFAULT_SEARCH_FILTER = new SearchFilter(Status.OPEN);
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
        result = prime * result + EntityUtil.hashCode(status);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            final SearchFilter other = (SearchFilter) obj;
            result = EntityUtil.bothNullOrEqual(status, other.getStatus());

        }
        return result;
    }
}