/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.argument;

import java.util.EnumSet;
import java.util.Set;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnConfidenceLevel;
import com.telenav.josm.common.entity.EntityUtil;


/**
 * Defines the filters that can be applied to the turn restriction layer.
 *
 * @author Beata
 * @version $Revision$
 */
public class TurnRestrictionFilter extends SearchFilter {

    private final Set<TurnConfidenceLevel> confidenceLevels;

    /** default filter to be applied if no filter is set/user reset's the filter */
    public static final TurnRestrictionFilter DEFAULT =
            new TurnRestrictionFilter(Status.OPEN, EnumSet.of(TurnConfidenceLevel.C1));


    /**
     * Builds a new filter with the given arguments.
     *
     * @param status defines the turn restriction status. Only turn restrictions/clusters that has the specified status
     * are displayed.
     * @param confidenceLevels defines the turn restriction confidence levels. Only turn restrictions/clusters that has
     * the specified confidence level are displayed.
     */
    public TurnRestrictionFilter(final Status status, final Set<TurnConfidenceLevel> confidenceLevels) {
        super(status);
        this.confidenceLevels = confidenceLevels;
    }


    public Set<TurnConfidenceLevel> getConfidenceLevels() {
        return confidenceLevels;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + super.hashCode();
        result = prime * result + EntityUtil.hashCode(confidenceLevels);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            final TurnRestrictionFilter other = (TurnRestrictionFilter) obj;
            result = super.equals(obj);
            result = result && EntityUtil.bothNullOrEqual(confidenceLevels, other.getConfidenceLevels());
        }
        return result;
    }
}