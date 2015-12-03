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
import org.openstreetmap.josm.plugins.improveosm.entity.TileType;


/**
 * Defines the filters that can be applied to the MissingGeometryLayer.
 *
 * @author Beata
 * @version $Revision$
 */
public class MissingGeometryFilter extends SearchFilter {

    /* default tile type to display */
    private static final EnumSet<TileType> DEFAULT_TYPE = EnumSet.of(TileType.ROAD);

    /** default search filter to be applied */
    public static final MissingGeometryFilter DEFAULT = new MissingGeometryFilter(Status.OPEN, DEFAULT_TYPE, null);

    private final EnumSet<TileType> types;
    private final Integer count;


    public MissingGeometryFilter(final Status status, final EnumSet<TileType> types, final Integer count) {
        super(status);
        this.types = types;
        this.count = count;
    }


    public EnumSet<TileType> getTypes() {
        return types;
    }

    public Integer getCount() {
        return count;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((count == null) ? 0 : count.hashCode());
        result = prime * result + ((types == null) ? 0 : types.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        boolean result = false;
        if (this == obj) {
            result = true;
        } else if (obj instanceof MissingGeometryFilter) {
            final MissingGeometryFilter other = (MissingGeometryFilter) obj;
            result = super.equals(other);
            result = result && ((count == null && other.getCount() == null)
                    || (count != null && count.equals(other.getCount())));
            result = result && ((types == null && other.getTypes() == null)
                    || (types != null && types.equals(other.getTypes())));
        }
        return result;
    }
}