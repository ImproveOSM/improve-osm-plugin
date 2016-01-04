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

import java.util.EnumSet;
import org.openstreetmap.josm.plugins.improveosm.entity.EntityUtil;
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnConfidenceLevel;


/**
 * Defines the filters that can be applied to the turn restriction layer.
 *
 * @author Beata
 * @version $Revision$
 */
public class TurnRestrictionFilter extends SearchFilter {

    private final EnumSet<TurnConfidenceLevel> confidenceLevels;

    /** default filter to be applied if no filter is set/user reset's the filter */
    public static final TurnRestrictionFilter DEFAULT = new TurnRestrictionFilter(Status.OPEN, null);


    /**
     * Builds a new filter with the given arguments.
     *
     * @param status defines the turn restriction status. Only turn restrictions/clusters that has the specified status
     * are displayed.
     * @param confidenceLevels defines the turn restriction confidence levels. Only turn restrictions/clusters that has
     * the specified confidence level are displayed.
     */
    public TurnRestrictionFilter(final Status status, final EnumSet<TurnConfidenceLevel> confidenceLevels) {
        super(status);
        this.confidenceLevels = confidenceLevels;
    }


    public EnumSet<TurnConfidenceLevel> getConfidenceLevels() {
        return confidenceLevels;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime + super.hashCode();
        result = prime * result + EntityUtil.hashCode(confidenceLevels);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj instanceof TurnRestrictionFilter) {
            final TurnRestrictionFilter other = (TurnRestrictionFilter) obj;
            result = super.equals(obj);
            result = result && EntityUtil.bothNullOrEqual(confidenceLevels, other.getConfidenceLevels());
        }
        return result;
    }
}