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
import org.openstreetmap.josm.plugins.improveosm.entity.Status;
import org.openstreetmap.josm.plugins.improveosm.entity.TurnConfidenceLevel;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public class TurnRestrictionFilter extends SearchFilter {

    private final EnumSet<TurnConfidenceLevel> confidenceLevels;

    public static final TurnRestrictionFilter DEFAULT = new TurnRestrictionFilter(Status.OPEN, null);


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
        result = prime * result + ((confidenceLevels == null) ? 0 : confidenceLevels.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj instanceof TurnRestrictionFilter) {
            final TurnRestrictionFilter other = (TurnRestrictionFilter) obj;
            result = super.equals(obj) && (confidenceLevels == null && other.getConfidenceLevels() == null)
                    || (confidenceLevels != null && confidenceLevels.equals(other.getConfidenceLevels()));
        }
        return result;
    }
}