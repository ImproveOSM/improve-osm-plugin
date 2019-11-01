/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.entity;

import java.util.Arrays;
import java.util.List;

/**
 * Defines the data layers available for this plugin.
 *
 * @author Beata
 * @version $Revision$
 */
public enum DataLayer {

    MISSING_GEOMETRY, DIRECTION_OF_FLOW, TURN_RESTRICTION;


    public static DataLayer getDataLayer(final String value) {
        final List<DataLayer> values = Arrays.asList(DataLayer.values());
        return values.stream().filter(m->m.toString().equals(value)).findAny().orElse(null);
    }
}