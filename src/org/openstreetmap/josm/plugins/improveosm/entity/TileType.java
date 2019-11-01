/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.entity;


/**
 * Defines the possible types a tile might have.
 *
 * @author Beata
 * @version $Revision$
 */
public enum TileType {

    ROAD,
    PARKING,
    BOTH {

        @Override
        public String displayValue() {
            return "mixed";
        }
    },
    WATER,
    PATH,
    MIXED;

    public String displayValue() {
        return name().toLowerCase();
    }

    @Override
    public String toString() {
        return "probable " + displayValue();
    }
}