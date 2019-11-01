/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
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