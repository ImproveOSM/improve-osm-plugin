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