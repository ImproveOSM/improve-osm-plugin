/*
 * Copyright 2019 Grabtaxi Holdings PTE LTE (GRAB), All rights reserved.
 *
 * Use of this source code is governed by an MIT-style license that can be found in the LICENSE file.
 *
 */
package org.openstreetmap.josm.plugins.improveosm.entity;

import org.openstreetmap.josm.plugins.improveosm.util.cnf.TurnRestrictionGuiConfig;

/**
 *
 * @author Beata
 * @version $Revision$
 */
public enum TurnConfidenceLevel {

    C1 {

        @Override
        public String toString() {
            return TurnRestrictionGuiConfig.getInstance().getLblC1();
        }
    },

    C2 {

        @Override
        public String toString() {
            return TurnRestrictionGuiConfig.getInstance().getLblC2();
        }
    };
}