/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.observer;

import org.openstreetmap.josm.plugins.improveosm.entity.TurnRestriction;


/**
 *
 * @author Beata
 * @version $Revision$
 */
public interface TurnRestrictionSelectionObserver {

    void selectSimpleTurnRestriction(TurnRestriction item);
}