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
import com.telenav.josm.common.entity.EntityUtil;


/**
 * Defines the filters that can be applied to the MissingGeometryLayer.
 *
 * @author Beata
 * @version $Revision$
 */
public class MissingGeometryFilter extends SearchFilter {

    private final EnumSet<TileType> types;
    private final Integer count;

    /** default search filter to be applied */
    public static final MissingGeometryFilter DEFAULT =
            new MissingGeometryFilter(SearchFilter.DEFAULT_SEARCH_FILTER.getStatus(), EnumSet.of(TileType.ROAD), null);


    /**
     * Builds a new filter with the given argument.
     *
     * @param status defines the tile/cluster status. Only tiles/clusters that has the specified status are displayed.
     * @param types defines the tile type. Only tiles/clusters with the specified types are displayed.
     * @param count defines the number of points/trips. Only clusters/tiles that has at least the given number of
     * points/trips are displayed.
     */
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
        result = prime * result + EntityUtil.hashCode(count);
        result = prime * result + EntityUtil.hashCode(types);
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
            result = result && EntityUtil.bothNullOrEqual(count, other.getCount());
            result = result && EntityUtil.bothNullOrEqual(types, other.getTypes());
        }
        return result;
    }
}