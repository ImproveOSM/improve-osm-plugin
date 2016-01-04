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
 * Utility class provides helper methods used by the entities.
 *
 * @author Bea
 * @version $Revision$
 */
public final class EntityUtil {

    private EntityUtil() {}


    /**
     * Verifies if the given objects are equals or both null.
     *
     * @param obj1 the first object
     * @param obj2 the second object
     * @return a boolean value
     */
    public static boolean bothNullOrEqual(final Object obj1, final Object obj2) {
        return (obj1 == null && obj2 == null) || (obj1 != null && obj1.equals(obj2));
    }

    /**
     * Computes the hashCode for the given object. If the object is null the method returns 0.
     *
     * @param obj an object
     * @return an integer value
     */
    public static int hashCode(final Object obj) {
        return (obj == null) ? 0 : obj.hashCode();
    }
}