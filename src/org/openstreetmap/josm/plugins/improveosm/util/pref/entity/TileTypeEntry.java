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
package org.openstreetmap.josm.plugins.improveosm.util.pref.entity;

import org.openstreetmap.josm.data.StructUtils.StructEntry;
import org.openstreetmap.josm.plugins.improveosm.entity.TileType;


/**
 * Preference entry corresponding to {@code TileType}.
 *
 * @author Beata
 * @version $Revision$
 */
public class TileTypeEntry {

    // preference entities must be declared public, otherwise JOSM preference loading does not work!
    @StructEntry
    private String name;

    public TileTypeEntry() {}

    /**
     * Builds a new object from the given type.
     *
     * @param type a {@code Type}
     */
    public TileTypeEntry(final TileType type) {
        this.name = type.name();
    }

    public String getName() {
        return name;
    }
}