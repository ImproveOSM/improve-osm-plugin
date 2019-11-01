/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.argument;

import java.util.EnumSet;
import java.util.Set;
import org.openstreetmap.josm.plugins.improveosm.entity.OnewayConfidenceLevel;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import com.telenav.josm.common.entity.EntityUtil;


/**
 * Defines the filters that can be applied to the TrafficFlowDirectionLayer.
 *
 * @author Beata
 * @version $Revision$
 */
public class OnewayFilter extends SearchFilter {

    private final Set<OnewayConfidenceLevel> confidenceLevels;

    /** default search filter */
    public static final OnewayFilter DEFAULT =
            new OnewayFilter(SearchFilter.DEFAULT_SEARCH_FILTER.getStatus(), EnumSet.of(OnewayConfidenceLevel.C1));


    /**
     * Builds a new object with the given arguments.
     *
     * @param status the road segment/cluster status
     * @param confidenceLevels the list of confidence levels
     */
    public OnewayFilter(final Status status, final Set<OnewayConfidenceLevel> confidenceLevels) {
        super(status);
        this.confidenceLevels = confidenceLevels;
    }


    public Set<OnewayConfidenceLevel> getConfidenceLevels() {
        return confidenceLevels;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = prime + super.hashCode();
        result = prime * result + EntityUtil.hashCode(confidenceLevels);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            final OnewayFilter other = (OnewayFilter) obj;
            result = super.equals(obj) && EntityUtil.bothNullOrEqual(confidenceLevels, other.getConfidenceLevels());
        }
        return result;
    }
}