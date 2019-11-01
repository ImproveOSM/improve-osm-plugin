/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.util.pref.entity;

import org.openstreetmap.josm.data.StructUtils.StructEntry;
import org.openstreetmap.josm.plugins.improveosm.entity.OnewayConfidenceLevel;


/**
 * Preference entry corresponding to the {@code OnewayConfidenceLevel} entity.
 *
 * @author Beata
 * @version $Revision$
 */
public class OnewayConfidenceLevelEntry {
    // preference entities must be declared public, otherwise JOSM preference loading does not work!
    @StructEntry
    private String name;

    /**
     * Builds an empty object.
     */
    public OnewayConfidenceLevelEntry() {}

    /**
     * Builds a new object based on the given confidence level.
     *
     * @param confidenceLevel a {@code ConfidenceLevel}
     */
    public OnewayConfidenceLevelEntry(final OnewayConfidenceLevel confidenceLevel) {
        this.name = confidenceLevel.name();
    }

    public String getName() {
        return name;
    }
}